package com.loopers.domain.payment;

import com.loopers.domain.shared.DomainEvent;

//public sealed interface PaymentEvent permits PaymentEvent.Completed, PaymentEvent.Canceled {
public class PaymentEvent {
    public record Completed(
            Long paymentId,
            Long orderId,
            Long userId,
            String method,
            String status,
            String reason
            ) implements DomainEvent {

        public static Completed from(Payment payment) {
            return new Completed(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getUserId(),
                    payment.getMethod().name(),
                    payment.getStatus().name(),
                    payment.getReason()
            );
        }
    }
    public record Canceled(
            Long paymentId,
            Long orderId,
            Long userId,
            String method,
            String status,
            String reason
    ) implements DomainEvent {
        public static Canceled from(Payment payment) {
            return new Canceled(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getUserId(),
                    payment.getMethod().name(),
                    payment.getStatus().name(),
                    payment.getReason()
            );
        }
    }
}
