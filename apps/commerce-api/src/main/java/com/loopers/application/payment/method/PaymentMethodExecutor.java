package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentManager;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.loopers.domain.payment.Payment.Method;
import static com.loopers.domain.payment.Payment.Status;

@Component
public class PaymentMethodExecutor {
    private final List<PaymentMethod> methods;
    private final PaymentManager paymentManager;

    public PaymentMethodExecutor(List<PaymentMethod> methods, PaymentManager paymentManager) {
        this.methods = methods;
        this.paymentManager = paymentManager;
    }

    public PaymentResult execute(PaymentCommand command) {
        paymentManager.updateStatus(command.getIdempotencyKey(), Status.PENDING);
        PaymentResult result = getPaymentMethod(command.getMethod())
                .pay(command);
        paymentManager.updateStatus(command.getIdempotencyKey(), result.getImmutable());
        return result;
    }

    private PaymentMethod getPaymentMethod(Method method){
        return methods.stream()
                .filter(m -> m.supports(method))
                .findFirst()
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 결제 방식입니다."));
    }

}
