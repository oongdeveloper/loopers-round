package com.loopers.application.catalog;

import com.loopers.domain.catalog.BrandCatalog;
import com.loopers.domain.catalog.BrandCatalogService;
import org.springframework.stereotype.Service;

@Service
public class BrandCatalogFacade {

    private final BrandCatalogService brandCatalogService;

    public BrandCatalogFacade(BrandCatalogService brandCatalogService) {
        this.brandCatalogService = brandCatalogService;
    }

    public BrandDetailInfo getBrandDetail(Long brandId){
        BrandCatalog brandCatalog = brandCatalogService.find(brandId);
        return BrandDetailInfo.from(brandCatalog);
    }
}
