package com.loopers.application.payment;


import com.loopers.application.payment.processor.PaymentPostProcessor;
import com.loopers.application.payment.processor.PaymentProcessor;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.pg.PgGatewayService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.loopers.domain.pg.PgPaymentInfo.TransactionResponse;

@Service
@Slf4j
public class PaymentFacade {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PgGatewayService pgGatewayService;
    private final PaymentProcessor paymentProcessor;
    private final PaymentPostProcessor postProcessor;

    public PaymentFacade(PaymentService paymentService, OrderService orderService, PgGatewayService pgGatewayService, PaymentProcessor paymentProcessor, PaymentPostProcessor postProcessor) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.pgGatewayService = pgGatewayService;
        this.paymentProcessor = paymentProcessor;
        this.postProcessor = postProcessor;
    }

    public PaymentResult pay(PaymentCommand command){
        try{
            if(!command.getIdempotencyKey().equals(paymentService.findIdempotencyKey(command)))
                throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 결제 키 요청입니다.");

            // TODO. Service 에서 Optional 을 안주면 내가 Controller 할 수가 없음.
//            Order order = orderService.findByIdAndUserId(command.getOrderId(), command.getUserId())
//                    .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 주문에 대한 결제 요청입니다."));
//
//            if (command.getAmount().compareTo(order.getFinalTotalPrice()) != 0)
//                throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 결제 요청입니다.");
            orderService.validateOrder(command.getOrderId(), command.getUserId(), command.getAmount());
            paymentService.findByKey(command.getIdempotencyKey()).
                    ifPresentOrElse(
                            payment -> PaymentStatusHandler.handleExistingPayment(payment.getStatus()),
                            () -> paymentService.save(Payment.of(command))
                    );

            // 결제 요청
            return paymentProcessor.process(command);
        } catch (Exception e) {
            log.error("결제 처리 오류 ", e);
            throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public void postPg(TransactionResponse.Data res){
        try{
            validatePgResponse(res);
            Payment payment = validateInternalPayment(res);

            PaymentResult paymentResult = PaymentResult.from(payment, res.status().name());
            postProcessor.postprocess(paymentResult);
        } catch (RuntimeException e){
            log.error("결제 콜백 오류 발생.", e);
            throw e;
        }
    }

    private void validatePgResponse(TransactionResponse.Data res) {
        TransactionResponse.Data data = pgGatewayService.checkTransaction(res.transactionKey());
        if (!res.status().equals(data.status()) || res.amount().compareTo(data.amount()) != 0) {
            throw new CoreException(ErrorType.BAD_REQUEST,
                    "잘못된 결제 Callback 입니다. " + res.transactionKey());
        }
    }

    private Payment validateInternalPayment(TransactionResponse.Data res) {
        return paymentService.findByKey(res.orderId())
                .filter(payment -> res.amount().compareTo(payment.getAmount()) == 0)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST,
                        "존재하지 않는 결제 이력이거나 결제 정보가 일치하지 않습니다. Callback 확인 필요 " + res.transactionKey()));
    }

    public String generateKey(Long userId, Long orderId){
        return paymentService.generateKey(userId, orderId).getKey();
    }
}
