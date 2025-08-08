package com.loopers.application.payment;

import java.math.BigDecimal;

public class PaymentCommand {

    public record Pay(
            Long orderId,
            BigDecimal paymentAmount
    ){}
}
