package com.loopers.infrastructure.catalog.query;

import com.loopers.application.catalog.query.BrandResult;
import com.loopers.application.catalog.query.BrandQuery;
import com.loopers.application.catalog.query.BrandQueryFacade;
import com.loopers.domain.catalog.Brand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class BrandQueryFacadeImpl implements BrandQueryFacade {
    private final BrandRepository brandRepository;

    public BrandQueryFacadeImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandResult getBrandDetail(BrandQuery.Detail query) {
//        Brand brand = brandRepositoryV2.findById(query.brandId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
        Brand brand = brandRepository.findById(query.brandId())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 브랜드입니다."));
        return new BrandResult(brand.getId(), brand.getBrandName(), brand.getLogoUrl());
        // TODO. BrandInfo 에서 Domain 을 알 이유가 없다.
//        return BrandInfo.of(brand);
    }
}
