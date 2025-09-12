package com.loopers.event.payload;

import com.loopers.application.metric.AggregateEvent;
import com.loopers.event.core.EventType;

import java.util.Collections;
import java.util.List;

public class ProductAppEvent {
    public record Clicked(
            Long userId,
            Long productId
    ) implements AggregateEvent {
        public static Clicked of(Long userId, Long productId){
            return new Clicked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_DETAIL_CLICKED;
        }

        @Override
        public List<Long> getProductIds() {
            return Collections.singletonList(this.productId);
        }
    }
}
