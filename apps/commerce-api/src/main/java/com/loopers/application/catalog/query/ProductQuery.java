package com.loopers.application.catalog.query;

import com.loopers.interfaces.api.catalog.ProductCatalogV1Dto;
import org.springframework.data.domain.Sort;

public class ProductQuery {
    public record ListQuery(
            Long brandId,
            ProductCatalogV1Dto.ProductCatalogSortBy sortBy,
            Sort.Direction direction
    ){
        public static ListQuery from(ProductCatalogV1Dto.ProductCatalogRequest request){
            return new ListQuery(
                    request.brandId(),
                    request.sortBy(),
                    request.direction()
            );
        }
    }
}
