package com.loopers.interfaces.api.ranking;

import com.loopers.application.product.ProductInfo;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class RankV1Dto {
    public record Summary(
            Long id,
            String brandName,
            String productName,
            BigDecimal price,
            String imageUrl,
            String description,
            ZonedDateTime publishedAt,
            Long likeCount
    ){
        public static Summary from(ProductInfo.DataList catalog){
            return new Summary(
                    catalog.id(),
                    catalog.brandName(),
                    catalog.productName(),
                    catalog.price(),
                    catalog.imageUrl(),
                    catalog.description(),
                    catalog.publishedAt(),
                    catalog.likeCount()
            );
        }
    }

}
