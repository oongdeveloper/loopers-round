package com.loopers.application.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.support.event.AppEvent;

import java.math.BigDecimal;

public class PaymentAppEvent {
    public record Reqeust(
            Long userId,
            Long orderId,
            String orderKey,
            BigDecimal amount,
            Payment.Method method
    ) implements AppEvent {
        public static Reqeust from(PaymentCommand command){
            return new Reqeust(
                    command.getUserId(),
                    command.getOrderId(),
                    command.getIdempotencyKey(),
                    command.getAmount(),
                    command.getMethod()
            );
        }
    }

    public record Completed(
            Long orderId,
            String orderKey,
            BigDecimal amount,
            Payment.Method method,
            Payment.Status status,
            String reason,
            int pgProvider,
            String pgTransactionKey
    ) implements AppEvent {
        public static Completed from(Payment payment){
            return new Completed(
                    payment.getOrderId(),
                    payment.getIdempotencyKey(),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getStatus(),
                    payment.getReason(),
                    payment.getPgProvider(),
                    payment.getPgTransactionId()
            );
        }
    }

    public record Failed(
            Long orderId,
            String orderKey,
            BigDecimal amount,
            Payment.Method method,
            Payment.Status status,
            String reason,
            int pgProvider,
            String pgTransactionKey
    ) implements AppEvent {
        public static Failed from(Payment payment){
            return new Failed(
                    payment.getOrderId(),
                    payment.getIdempotencyKey(),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getStatus(),
                    payment.getReason(),
                    payment.getPgProvider(),
                    payment.getPgTransactionId()
            );
        }
    }
}
