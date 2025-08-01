package com.loopers.domain.product;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductSkuService {
    private final ProductSkuRepository productSkuRepository;

    public ProductSkuService(ProductSkuRepository productSkuRepository) {
        this.productSkuRepository = productSkuRepository;
    }

    public List<ProductSku> findByProductCatalogId(Long productId){
        return productSkuRepository.findByProductCatalogId(productId);
    }

    public List<ProductSku> findByIds(Collection<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of();
        }
        return productSkuRepository.findByIdIn(skuIds);
    }
}
