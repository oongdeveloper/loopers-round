package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository jpaRepository;

    public BrandRepositoryImpl(BrandJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Brand> find(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Brand save(Brand brand) {
        return jpaRepository.save(brand);
    }

    @Override
    public List<Brand> findAllById(Iterable<Long> ids) {
        return jpaRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }


}
