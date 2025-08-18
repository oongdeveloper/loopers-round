package com.loopers.domain.product.projections;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface ProductListProjectionV3 {
    Long getId();
    Long getBrandId();
    String getProductName();
    BigDecimal getPrice();
    String getImageUrl();
    String getDescription();
    ZonedDateTime getPublishedAt();
    Long getLikeCount();
}
