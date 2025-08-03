package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateCommand(
        String userId,
        List<OrderItemCreateCommand> items
){
    public record OrderItemCreateCommand(
            Long productSkuId,
            Long productCatalogId,
            Long quantity,
            BigDecimal unitPrice,
            String productName
    ){

    }
}
