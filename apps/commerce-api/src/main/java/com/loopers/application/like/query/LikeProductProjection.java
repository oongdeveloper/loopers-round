package com.loopers.application.like.query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface LikeProductProjection {
    Long getId();
    Long getUserId();
    Long getProductCatalogId();
    ZonedDateTime getLikedAt();
    String getProductName();
    BigDecimal getBasePrice();
    String getImageUrl();
    String getBrandName();
}
