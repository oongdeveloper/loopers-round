package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository jpaRepository;

    public BrandRepositoryImpl(BrandJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Brand> findByIds(Collection<Long> brandIds) {
        return jpaRepository.findByIdIn(brandIds);
    }
}
