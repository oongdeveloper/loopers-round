package com.loopers.domain.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCatalogService {
    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductCatalog> findTop5ByBrandIdOrderByPublishedAtDesc(Long id) {
        return productRepository.findTop5ByBrandIdOrderByPublishedAtDesc(id);
    }

    public ProductCatalog save(ProductCatalog productCatalog) {
        return productRepository.save(productCatalog);
    }

    public Page<ProductCatalog> find(Optional<Long> brandId, Pageable pageable) {
        if (brandId.isPresent())
            return productRepository.findByBrandId(brandId.get(), pageable);
        else
            return productRepository.findAll(pageable);
    }

    public Optional<ProductCatalog> findById(Long productId) {
        return productRepository.find(productId);
    }
}
