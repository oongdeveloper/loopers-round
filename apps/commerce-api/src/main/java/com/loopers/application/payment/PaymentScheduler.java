package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.pg.PgGatewayService;
import com.loopers.domain.pg.PgSourceInfo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.loopers.domain.pg.PgSourceInfo.PgTransactionDetail.Status.FAILED;
import static com.loopers.domain.pg.PgSourceInfo.PgTransactionDetail.Status.SUCCESS;
import static com.loopers.domain.pg.PgSourceInfo.ResponseByOrderId;

@Slf4j
@Component
public class PaymentScheduler {
    private final PaymentService paymentService;
    private final PostPaymentHandler postPaymentHandler;
    private final PgGatewayService pgGatewayService;

    public PaymentScheduler(PaymentService paymentService, PostPaymentHandler postPaymentHandler, PgGatewayService pgGatewayService) {
        this.paymentService = paymentService;
        this.postPaymentHandler = postPaymentHandler;
        this.pgGatewayService = pgGatewayService;
    }

    @Scheduled(cron = "* * * * * *")
    @Transactional
    public void paymentSync(){
        List<Payment> payments = paymentService.findByStatus(Payment.Status.PENDING);
        for (Payment p : payments) {
            ResponseByOrderId result = pgGatewayService.checkOrderStatus(p.getIdempotencyKey());
            if(result.meta().result().equals(PgSourceInfo.MetaStatus.FAIL)){
                p.updateStatus(Payment.Status.FAILED);
                log.error("결제가 이루어지지 않음. paymentId : {}", p.getId());
                continue;
            }

            AtomicBoolean isDuplicated = new AtomicBoolean(false);
            result.transactionData().transactions().forEach(transaction -> {
                                if (transaction.status().equals(FAILED)) p.updateStatus(Payment.Status.FAILED);
                                else if (transaction.status().equals(SUCCESS)) {
                                    if(isDuplicated.get())
                                        log.error("결제 건이 1건 이상입니다. 확인이 필요합니다. orderId : {}, transactionId : {}",
                                                transaction.orderId(), transaction.transactionKey());
                                    p.updateStatus(Payment.Status.COMPLETED);
                                    isDuplicated.set(true);
                                }
                            });
            postPaymentHandler.postPayment(p);
        }
    }
}
