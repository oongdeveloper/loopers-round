package com.loopers.domain.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    long count();
    Optional<Product> find(Long id);
    List<Product> findTop5ByBrandIdOrderByPublishedAtDesc(Long id);
    Product save(Product product);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByBrandId(Long id, Pageable pageable);

    List<Product> findByIdIn(Collection<Long> ids);
}
