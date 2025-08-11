package com.loopers.application.catalog.query;

import org.springframework.data.domain.Page;

public interface ProductQueryFacade {

    Page<ProductResult.DataList> getProductList(ProductQuery.Summary query);

    ProductResult.DataDetail getProductDetail(ProductQuery.Detail query);
}
