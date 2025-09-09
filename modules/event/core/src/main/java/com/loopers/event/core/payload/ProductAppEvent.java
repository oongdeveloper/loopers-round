package com.loopers.event.core.payload;

import com.loopers.event.core.EventPayload;
import com.loopers.event.core.EventType;

public class ProductAppEvent {
    public record Clicked(
            Long userId,
            Long productId
    ) implements EventPayload {
        public static Clicked of(Long userId, Long productId){
            return new Clicked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_DETAIL_CLICKED;
        }
    }
}
