package com.loopers.application.refactor.catalog;

import org.springframework.data.domain.Page;

public interface ProductQueryFacade {

    Page<ProductInfo.DataList> getProductList(ProductQuery.ListQuery query);

    ProductInfo.DataDetail getProductDetail(ProductQuery.DetailQuery query);
}
