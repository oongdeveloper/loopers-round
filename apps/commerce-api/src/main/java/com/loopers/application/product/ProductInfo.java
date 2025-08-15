package com.loopers.application.product;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductInfo {
    public record DataList(
            Long id,
            String brandName,
            String productName,
            BigDecimal price,
            String imageUrl,
            String description,
            ZonedDateTime publishedAt,
            Long likeCount
    ){}

    public record DataDetail(
            Long productId,
            String brandName,
            String productName,
            BigDecimal price,
            String imageUrl,
            String description,
            ZonedDateTime publishedAt,
            Long likeCount,
            List<SkuInfo> skuInfos
    ){}

    public record SkuInfo(
            Long id,
            BigDecimal price,
            String imageUrl,
            String status,
            List<OptionDetail> optionDetails
    ){}

    public record OptionDetail(
            String optionName,
            String optionValue
    ){}
}
