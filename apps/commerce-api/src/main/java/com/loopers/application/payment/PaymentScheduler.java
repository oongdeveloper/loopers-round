package com.loopers.application.payment;

import com.loopers.application.payment.processor.PaymentPostProcessor;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.pg.PgGatewayService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.loopers.domain.pg.PgPaymentInfo.OrderResponse;
import static com.loopers.domain.pg.PgPaymentInfo.ResStatus.FAILED;
import static com.loopers.domain.pg.PgPaymentInfo.ResStatus.SUCCESS;

@Slf4j
@Component
public class PaymentScheduler {
    private final PaymentService paymentService;
    private final PgGatewayService pgGatewayService;
    private final PaymentPostProcessor postProcessor;

    public PaymentScheduler(PaymentService paymentService, PgGatewayService pgGatewayService, PaymentPostProcessor postProcessor) {
        this.paymentService = paymentService;
        this.pgGatewayService = pgGatewayService;
        this.postProcessor = postProcessor;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void paymentSync(){
        List<Payment> payments = paymentService.findByStatus(Payment.Status.PENDING);

        for (Payment p : payments) {
            OrderResponse.Data result = pgGatewayService.checkOrder(p.getIdempotencyKey());
            if (result == null) {
                p.updateStatus(Payment.Status.FAILED);
                log.error("결제가 이루어지지 않음. paymentId : {}", p.getId());
                continue;
            }

            AtomicBoolean isDuplicated = new AtomicBoolean(false);
            result.transactions().forEach(transaction -> {
                                if (transaction.status().equals(FAILED)) p.updateStatus(Payment.Status.FAILED);
                                else if (transaction.status().equals(SUCCESS)) {
                                    if(isDuplicated.get())
                                        log.error("결제 건이 1건 이상입니다. 확인이 필요합니다. orderId : {}, transactionId : {}",
                                                transaction.orderId(), transaction.transactionKey());
                                    p.updateStatus(Payment.Status.COMPLETED);
                                    isDuplicated.set(true);
                                }
                            });
            postProcessor.postprocess(PaymentResult.from(p,p.getStatus().name()));
        }
    }
}
