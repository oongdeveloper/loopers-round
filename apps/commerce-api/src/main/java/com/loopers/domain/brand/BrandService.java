package com.loopers.domain.brand;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand get(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "해당 ID의 브랜드를 찾을 수 없습니다."));
    }

    public List<Brand> getList(Collection<Long> brandIds) {
        return brandRepository.findByIds(brandIds);
    }
}
