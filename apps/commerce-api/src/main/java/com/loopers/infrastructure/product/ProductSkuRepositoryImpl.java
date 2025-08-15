package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ProductSkuRepositoryImpl implements ProductSkuRepository {
    @Override
    public List<ProductSku> findByProductCatalogId(Long id) {
        return List.of();
    }

    @Override
    public List<ProductSku> findByIdIn(Collection<Long> skuIds) {
        return List.of();
    }

    @Override
    public ProductSku save(ProductSku entity) {
        return null;
    }
}
