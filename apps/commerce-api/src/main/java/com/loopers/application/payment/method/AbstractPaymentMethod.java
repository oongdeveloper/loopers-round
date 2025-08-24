package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPaymentMethod implements PaymentMethod {

    abstract PaymentResult doPay(PaymentCommand command);

    public PaymentResult pay(PaymentCommand command) {
        try{
            return doPay(command);
        } catch (RuntimeException e){
            log.error("결제 중에 오류가 발생했습니다. ", e);
            return PaymentResult.from(command, null);
        }
    }
}
