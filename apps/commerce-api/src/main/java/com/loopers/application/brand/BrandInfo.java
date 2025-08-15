package com.loopers.application.brand;

public record BrandInfo(
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
