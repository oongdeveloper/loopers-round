package com.loopers.application.catalog;

import com.loopers.domain.catalog.Brand;

import java.util.List;

public record BrandDetailInfo (
        String brandName,
        String logoUrl,
        List<ProductCatalogInfo> products
){
    public static BrandDetailInfo from(Brand brand, List<ProductCatalogInfo> products) {
        return new BrandDetailInfo(
                brand.getBrandName(), brand.getLogoUrl(), products
        );
    }

}
