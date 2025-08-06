package com.loopers.infrastructure.refactor.like.projections;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface LikeProductProjection {
    Long getUserId();
    Long getProductCatalogId();
    String getBrandName();
    String getProductName();
    BigDecimal getPrice();
    String getImageUrl();
    String getDescription();
    ZonedDateTime getPublishedAt();
}
