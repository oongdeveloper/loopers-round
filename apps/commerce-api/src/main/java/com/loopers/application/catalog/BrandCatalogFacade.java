package com.loopers.application.catalog;

import com.loopers.domain.catalog.BrandCatalog;
import com.loopers.domain.catalog.BrandCatalogService;
import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandCatalogFacade {

    private final BrandCatalogService brandCatalogService;
    private final ProductCatalogService productCatalogService;

    public BrandCatalogFacade(BrandCatalogService brandCatalogService, ProductCatalogService productCatalogService) {
        this.brandCatalogService = brandCatalogService;
        this.productCatalogService = productCatalogService;
    }

    public BrandDetailInfo getBrandDetailWithProducts(Long brandId){
        BrandCatalog brandCatalog = brandCatalogService.find(brandId)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "해당 ID의 브랜드를 찾을 수 없습니다."));

        List<ProductCatalog> productCatalogs = productCatalogService.findTop5ByBrandIdOrderByPublishedAtDesc(brandId);

        List<ProductCatalogInfo> productCatalogInfos = productCatalogs.stream()
                .map(productCatalog -> ProductCatalogInfo.from(productCatalog, brandCatalog.getBrandName()))
                .collect(Collectors.toList());

        return BrandDetailInfo.from(brandCatalog, productCatalogInfos);
    }

}
