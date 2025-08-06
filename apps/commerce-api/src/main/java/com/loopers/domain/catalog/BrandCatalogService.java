package com.loopers.domain.catalog;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

@Service
public class BrandCatalogService {
    private final BrandRepository brandRepository;

    public BrandCatalogService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand find(Long id) {
        return brandRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "해당 ID의 브랜드를 찾을 수 없습니다."));
    }
}
