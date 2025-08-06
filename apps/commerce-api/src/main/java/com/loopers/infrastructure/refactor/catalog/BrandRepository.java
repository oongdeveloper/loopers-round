package com.loopers.infrastructure.refactor.catalog;

import com.loopers.domain.catalog.Brand;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BrandRepository extends Repository<Brand, Long> {

    Optional<Brand> findById(Long id);
}
