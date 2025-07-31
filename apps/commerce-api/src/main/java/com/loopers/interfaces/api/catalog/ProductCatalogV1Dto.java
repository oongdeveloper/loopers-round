package com.loopers.interfaces.api.catalog;

import org.springframework.data.domain.Sort;

public class ProductCatalogV1Dto {
    public record ProductCatalogRequest(
            int page,
            int size,
            ProductCatalogSortBy sortBy,
            Sort.Direction direction
    ){
        public static ProductCatalogRequest of(int page, int size, ProductCatalogSortBy sortBy, Sort.Direction direction){
            return new ProductCatalogRequest(page, size, sortBy, direction);
        }

    }

    public enum ProductCatalogSortBy{
        BASE_PRICE("basePrice"),
        PUBLISHED_AT("publishedAt");

        private final String propertyName;

        ProductCatalogSortBy(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName(){
            return propertyName;
        }
    }
}
