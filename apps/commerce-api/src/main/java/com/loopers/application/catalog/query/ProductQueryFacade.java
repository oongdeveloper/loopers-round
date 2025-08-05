package com.loopers.application.catalog.query;

import org.springframework.data.domain.Page;

public class ProductQueryFacade {
    private final ProductQueryRepository queryRepository;

    public ProductQueryFacade(ProductQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Page<ProductProjection> getProductList(ProductQuery request){
        return queryRepository.findProductList(request);
    }
}
