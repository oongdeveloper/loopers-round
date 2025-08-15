package com.loopers.application.like;

import org.springframework.data.domain.Pageable;

public class LikeQuery {
    public record Summary(
            Long userId,
            Pageable pageable

    ){
        public static Summary of(Long userId, Pageable pageable) {
            return new Summary(userId, pageable);
        }
    }
}
