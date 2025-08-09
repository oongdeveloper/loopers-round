package com.loopers.application.order;

import com.loopers.domain.order.Order;

import java.math.BigDecimal;

public record OrderInfo(
        Long id,
        BigDecimal finalTotalPrice,
        BigDecimal originalTotalPrice,
        String status
) {

    public static OrderInfo of(Order order) {
        return new OrderInfo(order.getId(), order.getFinalTotalPrice(), order.getOriginalTotalPrice(), order.getStatus());
    }
}
