package com.loopers.application.catalog.query;

public class BrandQuery {

    public record Detail(Long brandId){
        public static Detail of(Long brandId){
            return new Detail(brandId);
        }
    }
}
