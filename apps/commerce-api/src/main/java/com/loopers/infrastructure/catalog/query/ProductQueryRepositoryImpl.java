package com.loopers.infrastructure.catalog.query;

import com.loopers.application.catalog.query.ProductProjection;
import com.loopers.application.catalog.query.ProductQueryRepository;
import com.loopers.application.catalog.query.ProductQuery;
import org.springframework.data.domain.Page;

public class ProductQueryRepositoryImpl implements ProductQueryRepository {


    @Override
    public Page<ProductProjection> findProductList(ProductQuery query) {
        return null;
    }
}
