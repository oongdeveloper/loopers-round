package com.loopers.application.catalog.query;

import org.springframework.data.domain.Page;

public interface ProductQueryRepository {
    Page<ProductProjection>  findProductList(ProductQuery query);
}
