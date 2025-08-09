package com.loopers.application.catalog.query;

public record BrandResult(
        Long id,
        String brandName,
        String logoUrl
) {

//    public static BrandInfo of(Brand brand){
//        return new BrandInfo(
//                brand.getId(),
//                brand.getBrandName(),
//                brand.getLogoUrl()
//        );
//    }
}
