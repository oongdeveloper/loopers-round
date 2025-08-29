package com.loopers.application.order;

import com.loopers.support.event.AppEvent;

import java.util.Map;

public class OrderAppEvent {
    public record Created(
            Long userId,
            Map<Long, Long> items
    ) implements AppEvent {
        public static Created of(Long userId, Map<Long, Long> items){
            return new Created(userId, items);
        }
    }
}
