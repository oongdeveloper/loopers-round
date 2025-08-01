package com.loopers.application.catalog;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record ProductCatalogDetailInfo(
        Long productId,
        String brandName,
        String productName,
        BigDecimal basePrice,
        String imageUrl,
        String description,
        ZonedDateTime publishedAt,
        List<ProductCatalogDetailInfo.SkuInfo> skuInfos
){


    public record SkuInfo(
        Long skuId,
        BigDecimal unitPrice,
        String imageUrl,
        String status,
        List<ProductCatalogDetailInfo.OptionDetail> optionDetails
    ){
    }
    public record OptionDetail(
        String optionName,
        String optionValue
    ){
    }
}
