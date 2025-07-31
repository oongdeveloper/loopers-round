package com.loopers.domain.product;

import org.springframework.stereotype.Service;

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
}
