package com.loopers.event.payload;

import com.loopers.application.metric.AggregateEvent;
import com.loopers.event.core.EventType;

import java.util.Collections;
import java.util.List;

public class LikeAppEvent {
    public record Liked(
            Long userId,
            Long productId
    ) implements AggregateEvent {
        public static Liked of(Long userId, Long productId){
            return new Liked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_LIKED;
        }

        @Override
        public List<Long> getProductIds() {
            return Collections.singletonList(this.productId);
        }
    }

    public record UnLiked(
            Long userId,
            Long productId
    ) implements AggregateEvent {
        public static UnLiked of(Long userId, Long productId){
            return new UnLiked(userId, productId);
        }

        @Override
        public EventType getType() {
            return EventType.PRODUCT_UNLIKED;
        }

        @Override
        public List<Long> getProductIds() {
            return Collections.singletonList(this.productId);
        }
    }
}
