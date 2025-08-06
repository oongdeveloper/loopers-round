package com.loopers.infrastructure.refactor.catalog;

import com.loopers.application.refactor.catalog.BrandInfo;
import com.loopers.application.refactor.catalog.BrandQuery;
import com.loopers.application.refactor.catalog.BrandQueryFacade;
import com.loopers.domain.catalog.Brand;

public class BrandQueryFacadeImpl implements BrandQueryFacade {
    private final BrandRepository brandRepository;

    public BrandQueryFacadeImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandInfo getBrandDetail(BrandQuery.Detail query) {
        Brand brand = brandRepository.findById(query.brandId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
        return new BrandInfo(brand.getId(), brand.getBrandName(), brand.getLogoUrl());
        // TODO. BrandInfo 에서 Domain 을 알 이유가 없다.
//        return BrandInfo.of(brand);
    }
}
