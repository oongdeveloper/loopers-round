package com.loopers.application.like;

import com.loopers.support.event.AppEvent;

public class LikeAppEvent {
    public record Liked(
            Long userId,
            Long productId
    ) implements AppEvent {
        public static Liked of(Long userId, Long productId){
            return new Liked(userId, productId);
        }
    }

    public record UnLiked(
            Long userId,
            Long productId
    ) implements AppEvent {
        public static UnLiked of(Long userId, Long productId){
            return new UnLiked(userId, productId);
        }
    }
}
