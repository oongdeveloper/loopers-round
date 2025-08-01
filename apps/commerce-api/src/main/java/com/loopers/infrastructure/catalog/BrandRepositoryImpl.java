package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.BrandCatalog;
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
    public Optional<BrandCatalog> find(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public BrandCatalog save(BrandCatalog brandCatalog) {
        return jpaRepository.save(brandCatalog);
    }

    @Override
    public List<BrandCatalog> findAllById(Iterable<Long> ids) {
        return jpaRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }


}
