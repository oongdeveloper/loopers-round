package com.loopers.application.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.product.Product;

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

    public LikeProductDetailInfo(Like productLike, Product product) {
        this.likeId = productLike.getId().getProductId();
        this.userId = productLike.getId().getUserId();
        this.productCatalogId = productLike.getId().getProductId();
        this.likedAt = productLike.getCreatedAt();

        if (product != null) {
            this.productName = product.getProductName();
            this.basePrice = product.getPrice();
            this.imageUrl = product.getImageUrl();
            this.description = product.getDescription();
            this.brandId = product.getBrandId();
        }
    }
}
