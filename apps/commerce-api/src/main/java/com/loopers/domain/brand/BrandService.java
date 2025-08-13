package com.loopers.domain.brand;


import com.loopers.infrastructure.brand.BrandRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand find(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "해당 ID의 브랜드를 찾을 수 없습니다."));
    }
}
