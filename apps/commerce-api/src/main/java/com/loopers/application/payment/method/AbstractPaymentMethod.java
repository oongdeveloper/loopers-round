package com.loopers.application.payment.method;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.PaymentCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPaymentMethod implements PaymentMethod{

    abstract PaymentResult doPay(PaymentCommand command);

    public PaymentResult pay(PaymentCommand command) {
        log.info("주문번호 : {}, 멱등키 {}, 결제방식 {} 으로 결제 시도합니다.", command.getOrderId(), command.getIdempotencyKey(), command.getMethod());
        return doPay(command);
    }
}
