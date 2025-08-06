package com.loopers.application.refactor.catalog;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class ProductQuery {
    public record ListQuery(
            Long brandId,
            SortType type,
            Pageable pageable

    ){
        public enum SortType{
                LATEST,
                PRICE_ASC,
                LIKES_DESC
        }

        public ListQuery(Long brandId, String sort, Pageable pageable){
            this(
                    brandId,
                    Arrays.stream(SortType.values())
                            .filter(type -> type.name().equalsIgnoreCase(sort))
                            .findFirst()
                            .orElse(SortType.LATEST),
                    pageable
            );
        }
    }

    public record DetailQuery(Long productId){
        public static DetailQuery of(Long productId){
            return new DetailQuery(productId);
        }

    }
}
