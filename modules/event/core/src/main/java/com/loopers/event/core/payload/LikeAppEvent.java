package com.loopers.event.core.payload;

import com.loopers.event.core.EventPayload;
import com.loopers.event.core.EventType;

public class LikeAppEvent {
    public record Liked(
            Long userId,
            Long productId
    ) implements EventPayload {
        public static Liked of(Long userId, Long productId){
            return new Liked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_LIKED;
        }
    }

    public record UnLiked(
            Long userId,
            Long productId
    ) implements EventPayload {
        public static UnLiked of(Long userId, Long productId){
            return new UnLiked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_UNLIKED;
        }
    }
}
