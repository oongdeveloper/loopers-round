package com.loopers.event.core.payload;

import com.loopers.event.core.EventPayload;
import com.loopers.event.core.EventType;

import java.math.BigDecimal;
import java.util.Map;

public class OrderAppEvent {
    public record Created(
            Long orderId,
            Long userId,
            Long couponId,
            BigDecimal finalPrice,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements EventPayload {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }

    public record Completed(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements EventPayload {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }

    public record Canceled(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements EventPayload {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }
    }
}
