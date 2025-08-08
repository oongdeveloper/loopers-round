package com.loopers.application.catalog.query;

import org.springframework.data.domain.Page;

public interface ProductQueryFacade {

    Page<ProductInfo.DataList> getProductList(ProductQuery.Summary query);

    ProductInfo.DataDetail getProductDetail(ProductQuery.Detail query);
}
