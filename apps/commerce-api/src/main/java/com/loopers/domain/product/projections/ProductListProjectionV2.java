package com.loopers.domain.product.projections;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface ProductListProjectionV2 {
    Long getId();
    String getBrandName();
    String getProductName();
    BigDecimal getPrice();
    String getImageUrl();
    String getDescription();
    ZonedDateTime getPublishedAt();
    Long getLikeCount();
}
