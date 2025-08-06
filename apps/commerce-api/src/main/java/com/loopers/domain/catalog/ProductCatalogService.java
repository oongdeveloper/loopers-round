package com.loopers.domain.catalog;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCatalogService {
    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findTop5ByBrandIdOrderByPublishedAtDesc(Long id) {
        return productRepository.findTop5ByBrandIdOrderByPublishedAtDesc(id);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Page<Product> find(Optional<Long> brandId, Pageable pageable) {
        if (brandId.isPresent())
            return productRepository.findByBrandId(brandId.get(), pageable);
        else
            return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.find(productId);
    }

    public List<Product> findByIds(Collection<Long> productCatalogIds) {
        if (productCatalogIds == null || productCatalogIds.isEmpty()) {
            return List.of(); // 빈 목록이 들어오면 빈 리스트 반환
        }
        return productRepository.findByIdIn(productCatalogIds);
    }
}
