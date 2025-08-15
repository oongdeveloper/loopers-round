package com.loopers.application.product;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class ProductQuery {
    public record Summary(
            Long brandId,
            SortType type,
            Pageable pageable

    ){
        public enum SortType{
                LATEST,
                PRICE_ASC,
                LIKES_DESC
        }

        public Summary(Long brandId, String sort, Pageable pageable){
            this(
                    brandId,
                    Arrays.stream(SortType.values())
                            .filter(type -> type.name().equalsIgnoreCase(sort))
                            .findFirst()
                            .orElse(SortType.LATEST),
                    pageable
            );
        }

        public static Summary of(Long brandId, String sort, Pageable pageable){
            return new Summary(brandId, sort, pageable);
        }
    }

    public record Detail(Long productId){
        public static Detail of(Long productId){
            return new Detail(productId);
        }

    }
}
