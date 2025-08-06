package com.loopers.application.refactor.like;

import org.springframework.data.domain.Pageable;

public class LikeQuery {
    public record ListQuery(
            Long userId,
            Pageable pageable

    ){
        public static ListQuery of(Long userId, Pageable pageable) {
            return new ListQuery(userId, pageable);
        }
    }
}
