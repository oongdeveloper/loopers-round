package com.loopers.domain.payment;

public sealed interface PaymentEvent permits PaymentEvent.Completed, PaymentEvent.Canceled {
    record Completed(
            Long paymentId,
            Long orderId,
            Long userId,
            String method,
            String status,
            String reason
            ) implements PaymentEvent {

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
    record Canceled(
            Long paymentId,
            Long orderId,
            Long userId,
            String method,
            String status,
            String reason
    ) implements PaymentEvent {
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
