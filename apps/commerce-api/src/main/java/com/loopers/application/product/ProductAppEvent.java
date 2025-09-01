package com.loopers.application.product;

import com.loopers.support.event.AppEvent;

public class ProductAppEvent {
    public record Clicked(
            Long userId,
            Long productId
    ) implements AppEvent {
        public static Clicked of(Long userId, Long productId){
            return new Clicked(userId, productId);
        }
    }
}
