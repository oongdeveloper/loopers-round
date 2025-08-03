package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ProductSkuRepositoryImpl implements ProductSkuRepository {
    private final ProductSkuJpaRepository jpaRepository;

    public ProductSkuRepositoryImpl(ProductSkuJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ProductSku> findByProductCatalogId(Long id) {
        return jpaRepository.findByProductCatalogIdWithAllOptions(id);
    }

    @Override
    public List<ProductSku> findByIdIn(Collection<Long> skuIds) {
        return jpaRepository.findByIdIn(skuIds);
    }

    @Override
    public ProductSku save(ProductSku entity) {
        return jpaRepository.save(entity);
    }
}
