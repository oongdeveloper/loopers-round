package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;

public interface PaymentMethod {

    boolean supports(Payment.Method method);
    PaymentResult pay(PaymentCommand command);
}
