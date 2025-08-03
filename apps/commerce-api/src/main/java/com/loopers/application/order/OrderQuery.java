package com.loopers.application.order;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderQuery {

    public record ListQuery(
            Long orderId,
            String userId,
            BigDecimal totalOrderPrice,
            String status,
            ZonedDateTime orderedAt
    ){}

    public record DetailQuery(
            Long orderId,
            String userId,
            BigDecimal totalOrderPrice,
            String status,
            ZonedDateTime orderedAt,
            List<ItemDetailQuery> items
    ){}

    public record ItemDetailQuery(
            Long orderItemId,
            Long productSkuId,
            Long productCatalogId,
            Long quantity,
            BigDecimal totalItemPrice,
            String productName,
            String brandName,
            BigDecimal unitPrice
    ){}
}
