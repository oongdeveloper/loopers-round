package com.loopers.domain.stock;

import java.util.Map;

public class StockCommand {
    public record Reduce(
            Long orderId,
            Map<Long, Long> items
    ) {
        public static Reduce of(Long orderId, Map<Long, Long> items){
            return new Reduce(orderId, items);
        }

    }

    public record Restore(
            Long orderId,
            Map<Long, Long> items
    ){
        public static Restore of(Long orderId, Map<Long, Long> items){
            return new Restore(orderId, items);
        }

    }
}
