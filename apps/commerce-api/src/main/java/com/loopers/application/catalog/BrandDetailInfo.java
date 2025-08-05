package com.loopers.application.catalog;

import com.loopers.domain.catalog.BrandCatalog;

public record BrandDetailInfo (
        Long brandId,
        String brandName,
        String logoUrl
){
    public static BrandDetailInfo from(BrandCatalog brandCatalog) {
        return new BrandDetailInfo(
                brandCatalog.getId(), brandCatalog.getBrandName(), brandCatalog.getLogoUrl()
        );
    }

}
