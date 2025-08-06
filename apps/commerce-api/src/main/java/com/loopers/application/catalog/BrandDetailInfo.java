package com.loopers.application.catalog;

import com.loopers.domain.catalog.Brand;

public record BrandDetailInfo (
        Long brandId,
        String brandName,
        String logoUrl
){
    public static BrandDetailInfo from(Brand brand) {
        return new BrandDetailInfo(
                brand.getId(), brand.getBrandName(), brand.getLogoUrl()
        );
    }

}
