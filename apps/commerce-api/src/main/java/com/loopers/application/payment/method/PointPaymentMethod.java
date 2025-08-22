package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import org.springframework.stereotype.Component;

@Component
public class PointPaymentMethod extends AbstractPaymentMethod {
    private final PointService pointService;

    public PointPaymentMethod(PointService pointService) {
        this.pointService = pointService;
    }

    @Override
    public boolean supports(Payment.Method method) {
        return Payment.Method.POINT.equals(method);
    }

    @Override
    PaymentResult doPay(PaymentCommand command) {
        try{
            pointService.deduct(PointCommand.Deduct.of(command.getUserId(), command.getAmount()));

            return new PaymentResult(
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod(),
                    PaymentResult.Status.COMPLETED,
                    "정상 결제되었습니다.",
                    null,
                    null
            );
        } catch (RuntimeException e){
            return new PaymentResult(
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod(),
                    PaymentResult.Status.FAILED,
                    e.getMessage(),
                    null,
                    null
            );
        }
    }

}
