package com.loopers.application.refactor.catalog;

public class BrandQuery {

    public record Detail(Long brandId){
        public static Detail of(Long brandId){
            return new Detail(brandId);
        }
    }
}
