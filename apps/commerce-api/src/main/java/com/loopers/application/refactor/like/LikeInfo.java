package com.loopers.application.refactor.like;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class LikeInfo {
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
