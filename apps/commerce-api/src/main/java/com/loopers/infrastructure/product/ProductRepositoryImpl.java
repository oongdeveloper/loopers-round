package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.projections.ProductListProjection;
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
    public Page<ProductListProjection> findByBrandId(Long brandId, String sort, Pageable pageable) {
        return jpaRepository.findByBrandId(brandId, sort, pageable);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return jpaRepository.findById(productId);
    }

    @Override
    public List<Product> findProductsBySkuIds(Collection<Long> skuIds) {
        return jpaRepository.findProductsBySkuIds(skuIds);
    }
}
