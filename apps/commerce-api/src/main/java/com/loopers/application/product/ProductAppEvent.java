package com.loopers.application.product;

import com.loopers.event.core.EventType;
import com.loopers.support.event.AppEvent;

public class ProductAppEvent {
    public record Clicked(
            Long userId,
            Long productId
    ) implements AppEvent {
        public static Clicked of(Long userId, Long productId){
            return new Clicked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_DETAIL_CLICKED;
        }
    }
}
