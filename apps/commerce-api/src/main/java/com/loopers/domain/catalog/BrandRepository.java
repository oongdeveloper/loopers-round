package com.loopers.domain.catalog;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<BrandCatalog> find(Long id);
    BrandCatalog save(BrandCatalog user);
    List<BrandCatalog> findAllById(Iterable<Long> ids);
    long count();
}
