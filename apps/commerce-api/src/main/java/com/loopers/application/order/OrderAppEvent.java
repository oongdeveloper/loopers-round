package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.event.core.EventType;
import com.loopers.support.event.AppEvent;

import java.math.BigDecimal;
import java.util.Map;

public class OrderAppEvent {
//    public record Created(
//            Long userId,
//            Map<Long, Long> items
//    ) implements AppEvent {
//        public static Created of(Long userId, Map<Long, Long> items){
//            return new Created(userId, items);
//        }
//
//        @Override
//        public EventType getType() {
//            return EventType.ORDER_CREATED;
//        }
//    }

    public record Created(
            Long orderId,
            Long userId,
            Long couponId,
            BigDecimal finalPrice,
            Map<Long, Long> orderedItems
    ) implements AppEvent {
        public static Created from(Order order){
            return new Created(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getFinalTotalPrice(),
                    order.getLines().getOrderLineQuantity()
            );
        }
        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }

    public record Completed(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems
    ) implements AppEvent {
        public static Completed from(Order order) {
            return new Completed(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getLines().getOrderLineQuantity()
            );
        }

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }

    public record Canceled(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems
    ) implements AppEvent {
        public static Canceled from(Order order) {
            return new Canceled(
                    order.getId(),
                    order.getUserId(),
                    order.getCouponId(),
                    order.getLines().getOrderLineQuantity()
            );
        }

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }
}
