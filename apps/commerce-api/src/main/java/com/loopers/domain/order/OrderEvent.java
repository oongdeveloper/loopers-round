package com.loopers.domain.order;

import com.loopers.domain.shared.DomainEvent;

import java.math.BigDecimal;
import java.util.Map;

public class OrderEvent {
    public record Created(
            Long orderId,
            Long userId,
            Long couponId,
            BigDecimal finalPrice,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements DomainEvent {
        public static Created from(Order order){
            return new Created(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getFinalTotalPrice(),
                    order.getLines().getOrderLineQuantity(),
                    order.getLines().getOrderLineProductId()
            );
        }
    }

    public record Completed(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements DomainEvent {
        public static Completed from(Order order) {
            return new Completed(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getLines().getOrderLineQuantity(),
                    order.getLines().getOrderLineProductId()
            );
        }
    }

    public record Canceled(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements DomainEvent {
        public static Canceled from(Order order) {
            return new Canceled(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getLines().getOrderLineQuantity(),
                    order.getLines().getOrderLineProductId()
            );
        }
    }
}
