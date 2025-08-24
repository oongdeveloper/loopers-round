package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;

import java.math.BigDecimal;

public record PaymentResult(
        Long orderId,
        String idempotencyKey,
        BigDecimal amount,
        Payment.Method method,
        Status status,
        String reason,
        Integer pgProvider,
        String pgTransactionKey
) {
    public static PaymentResult from(Payment payment, String status) {
        return new PaymentResult(
                payment.getOrderId(),
                payment.getIdempotencyKey(),
                payment.getAmount(),
                payment.getMethod(),
                Status.valueOf(status),
                payment.getReason(),
                payment.getPgProvider(),
                payment.getPgTransactionId()
        );
    }

    public static PaymentResult from(PaymentCommand command, String status) {
        return new PaymentResult(
                command.getOrderId(),
                command.getIdempotencyKey(),
                command.getAmount(),
                command.getMethod(),
                Status.valueOf(status),
                null,
                null,
                null
        );
    }

    public enum Status{
        FAILED,
        COMPLETED,
        CANCELED
    }
}
