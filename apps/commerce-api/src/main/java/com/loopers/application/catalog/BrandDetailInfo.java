package com.loopers.application.catalog;

import com.loopers.domain.catalog.BrandCatalog;

import java.util.List;

public record BrandDetailInfo (
        String brandName,
        String logoUrl,
        List<ProductCatalogInfo> products
){
    public static BrandDetailInfo from(BrandCatalog brandCatalog, List<ProductCatalogInfo> products) {
        return new BrandDetailInfo(
                brandCatalog.getBrandName(), brandCatalog.getLogoUrl(), products
        );
    }

}
