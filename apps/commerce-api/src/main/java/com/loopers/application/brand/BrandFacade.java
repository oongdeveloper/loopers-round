package com.loopers.application.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class BrandFacade {
    private final BrandJpaRepository brandJpaRepository;

    public BrandFacade(BrandJpaRepository brandJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
    }

    public BrandInfo getBrandDetail(BrandQuery.Detail query) {
        Brand brand = brandJpaRepository.findById(query.brandId())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 브랜드입니다."));
        return new BrandInfo(brand.getId(), brand.getBrandName(), brand.getLogoUrl());
        // TODO. BrandInfo 에서 Domain 을 알 이유가 없다.
//        return BrandInfo.of(brand);
    }
}
