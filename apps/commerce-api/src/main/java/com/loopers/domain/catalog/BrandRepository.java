package com.loopers.domain.catalog;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> find(Long id);
    Brand save(Brand user);
    List<Brand> findAllById(Iterable<Long> ids);
    long count();
}
