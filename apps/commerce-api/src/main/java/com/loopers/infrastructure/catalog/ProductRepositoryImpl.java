package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
    public Optional<Product> find(Long id) {
        return jpaRepository.findById(id);
    }

    public List<Product> findTop5ByBrandIdOrderByPublishedAtDesc(Long id){
        return jpaRepository.findTop5ByBrandIdOrderByPublishedAtDesc(id);
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByBrandId(Long id, Pageable pageable) {
        return jpaRepository.findByBrandId(id,pageable);
    }

    @Override
    public List<Product> findByIdIn(Collection<Long> ids) {
        return jpaRepository.findByIdIn(ids);
    }
}
