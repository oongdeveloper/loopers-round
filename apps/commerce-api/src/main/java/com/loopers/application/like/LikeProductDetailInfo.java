package com.loopers.application.like;

import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.like.Like;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class LikeProductDetailInfo {
    private Long likeId;
    private Long userId;
    private Long productCatalogId;
    private ZonedDateTime likedAt;

    private String productName;
    private BigDecimal basePrice;
    private String imageUrl;
    private String description;
    private Long brandId;

    public LikeProductDetailInfo(Like productLike, ProductCatalog productCatalog) {
        this.likeId = productLike.getId().getProductCatalogId();
        this.userId = productLike.getId().getUserId();
        this.productCatalogId = productLike.getId().getProductCatalogId();
        this.likedAt = productLike.getCreatedAt();

        if (productCatalog != null) {
            this.productName = productCatalog.getProductName();
            this.basePrice = productCatalog.getBasePrice();
            this.imageUrl = productCatalog.getImageUrl();
            this.description = productCatalog.getDescription();
            this.brandId = productCatalog.getBrandId();
        }
    }
}
