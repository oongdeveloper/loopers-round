package com.loopers.application.stock;

import com.loopers.domain.notify.AppErrorEvent;

import java.util.Map;

public record StockRestoreFailedEvent (
        Long orderId,
        Map<Long, Long> items,
        String reason
) implements AppErrorEvent {
    public static StockRestoreFailedEvent of(Long orderId, Map<Long, Long> items, String reason){
        return new StockRestoreFailedEvent(orderId, items, reason);
    }
}
