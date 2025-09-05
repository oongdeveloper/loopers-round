package com.loopers.domain.stock;

import com.loopers.domain.shared.DomainEvent;

public class StockEvent {
    public record OnSale(
            Long productId,
            Long productSkuId
    ) implements DomainEvent {
        public static OnSale from(Stock stock){
            return new OnSale(
                    stock.getProductId(),
                    stock.getProductSkuId()
            );
        }
    }

    public record OutOfStock(
            Long productId,
            Long productSkuId
    ) implements DomainEvent {
        public static OutOfStock from(Stock stock){
            return new OutOfStock(
                    stock.getProductId(),
                    stock.getProductSkuId()
            );
        }
    }
}
