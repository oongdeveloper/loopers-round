package com.loopers.interfaces.api.catalog;

import org.springframework.data.domain.Sort;

public class ProductCatalogV1Dto {
    public record ProductCatalogRequest(
            Long brandId,
            int page,
            int size,
            ProductCatalogSortBy sortBy,
            Sort.Direction direction
    ){
        public static ProductCatalogRequest of(Long brandId, int page, int size, ProductCatalogSortBy sortBy, Sort.Direction direction){
            return new ProductCatalogRequest(brandId, page, size, sortBy, direction);
        }
    }

    public enum ProductCatalogSortBy{
        RECENT,
        LOW_PRICE,
        LIKE
    }
}
