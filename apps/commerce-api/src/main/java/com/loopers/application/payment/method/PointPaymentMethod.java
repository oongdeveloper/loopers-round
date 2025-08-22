package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.point.exception.InsufficientPointsException;
import org.springframework.stereotype.Component;

@Component
public class PointPaymentMethod extends AbstractPaymentMethod{
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
        PaymentResult paymentResult = new PaymentResult(
                command.getOrderId(),
                command.getIdempotencyKey(),
                command.getAmount(),
                command.getMethod().name()
                );

        try{
            pointService.deduct(PointCommand.Deduct.of(command.getUserId(), command.getAmount()));

            paymentResult.setSuccess(true);
            paymentResult.setStatus(PaymentResult.Status.COMPLETED);
        } catch (InsufficientPointsException e){
            paymentResult.setSuccess(false);
            paymentResult.setStatus(PaymentResult.Status.FAILED);
            paymentResult.setReason(e.getMessage());
        } catch (RuntimeException e){
            paymentResult.setSuccess(false);
            paymentResult.setStatus(PaymentResult.Status.SYSTEM_FAILED);
        }
        return paymentResult;
    }

}
