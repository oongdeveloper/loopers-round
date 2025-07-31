package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.catalog.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public Optional<ProductCatalog> find(Long id) {
        return jpaRepository.findById(id);
    }

    public List<ProductCatalog> findTop5ByBrandIdOrderByPublishedAtDesc(Long id){
        return jpaRepository.findTop5ByBrandIdOrderByPublishedAtDesc(id);
    }

    @Override
    public ProductCatalog save(ProductCatalog product) {
        return jpaRepository.save(product);
    }

    @Override
    public Page<ProductCatalog> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<ProductCatalog> findByBrandId(Long id, Pageable pageable) {
        return jpaRepository.findByBrandId(id,pageable);
    }
}
