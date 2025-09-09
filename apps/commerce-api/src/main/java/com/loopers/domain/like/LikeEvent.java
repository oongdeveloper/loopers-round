package com.loopers.domain.like;

import com.loopers.domain.shared.DomainEvent;

public class LikeEvent {
    public record Liked(
            Long userId,
            Long productId
    ) implements DomainEvent {
        public static Liked from(Like like) {
            return new Liked(like.getId().getUserId(), like.getId().getProductId());
        }
    }

    public record UnLiked(
            Long userId,
            Long productId
    ) implements DomainEvent {
        public static UnLiked from(Like like) {
            return new UnLiked(like.getId().getUserId(), like.getId().getProductId());
        }
    }
}
