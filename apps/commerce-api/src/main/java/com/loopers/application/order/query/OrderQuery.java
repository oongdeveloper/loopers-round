package com.loopers.application.order.query;

import org.springframework.data.domain.Pageable;

public class OrderQuery {
    public record Summary(
            Long userId,
            Pageable pageable
    ){
        public static Summary of(Long userId, Pageable pageable) {
            return new Summary(userId, pageable);
        }
    }

    public record Detail(
            Long orderId
    ){
        public static Detail of(Long orderId){
            return new Detail(orderId);
        }
    }
}
