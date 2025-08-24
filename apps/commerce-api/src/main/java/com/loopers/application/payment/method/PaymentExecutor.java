package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.loopers.domain.payment.Payment.Method;

@Component
public class PaymentExecutor {
    private final List<PaymentMethod> methods;

    public PaymentExecutor(List<PaymentMethod> methods) {
        this.methods = methods;
    }

    public PaymentResult execute(PaymentCommand command) {
        return getPaymentMethod(command.getMethod())
                .pay(command);
    }

    private PaymentMethod getPaymentMethod(Method method){
        return methods.stream()
                .filter(m -> m.supports(method))
                .findFirst()
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 결제 방식입니다."));
    }

}
