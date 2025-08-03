package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.catalog.ProductCatalogV1Dto;
import org.springframework.data.domain.Sort;

public class LikeV1Dto {
    public record LikeRequest(
            int page,
            int size,
            ProductCatalogV1Dto.ProductCatalogSortBy sortBy,
            Sort.Direction direction
    ){
        public static LikeV1Dto.LikeRequest of(int page, int size, ProductCatalogV1Dto.ProductCatalogSortBy sortBy, Sort.Direction direction){
            return new LikeV1Dto.LikeRequest(page, size, sortBy, direction);
        }
    }
}
