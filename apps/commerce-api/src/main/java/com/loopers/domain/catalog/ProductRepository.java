package com.loopers.domain.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    long count();
    Optional<ProductCatalog> find(Long id);
    List<ProductCatalog> findTop5ByBrandIdOrderByPublishedAtDesc(Long id);
    ProductCatalog save(ProductCatalog product);

    Page<ProductCatalog> findAll(Pageable pageable);

    Page<ProductCatalog> findByBrandId(Long id, Pageable pageable);

    List<ProductCatalog> findByIdIn(Collection<Long> ids);
}
