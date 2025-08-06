package com.loopers.application.refactor.order;

import org.springframework.data.domain.Pageable;

public class OrderQuery {
    public record ListQuery(
            Long userId,
            Pageable pageable
    ){
        public static ListQuery of(Long userId, Pageable pageable) {
            return new ListQuery(userId, pageable);
        }
    }

    public record DetailQuery(
            Long orderId
    ){
        public static DetailQuery of(Long orderId){
            return new DetailQuery(orderId);
        }
    }
}
