package com.loopers.event.payload;

import com.loopers.application.metric.AggregateEvent;
import com.loopers.event.core.EventType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderAppEvent {
    public record Created(
            Long orderId,
            Long userId,
            Long couponId,
            BigDecimal finalPrice,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements AggregateEvent {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }

        @Override
        public List<Long> getProductIds() {
            return orderedProductId.values().stream().toList();
        }

    }

    public record Completed(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements AggregateEvent {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }

        @Override
        public List<Long> getProductIds() {
            return orderedProductId.values().stream().toList();
        }
    }

    public record Canceled(
            Long orderId,
            Long userId,
            Long couponId,
            Map<Long, Long> orderedItems,
            Map<Long, Long> orderedProductId
    ) implements AggregateEvent {

        @Override
        public EventType getType() {
            return EventType.ORDER_CREATED;
        }

        @Override
        public List<Long> getProductIds() {
            return orderedProductId.values().stream().toList();
        }
    }
}
