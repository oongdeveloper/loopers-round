package com.loopers.application.catalog.query;

import java.math.BigDecimal;

public interface ProductProjection {
    Long getId();
    String getName();
    String getBrandName();
    BigDecimal getBasePrice();
    String getImageUrl();
    String getCreatedAt();
    Long getLikeCount();
}
