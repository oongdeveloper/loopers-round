package com.loopers.application.like.query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class LikeResult {
    public record DataList(
            Long userId,
            Long productCatalogId,
            String brandName,
            String productName,
            BigDecimal price,
            String imageUrl,
            String description,
            ZonedDateTime publishedAt
    ){}
}
