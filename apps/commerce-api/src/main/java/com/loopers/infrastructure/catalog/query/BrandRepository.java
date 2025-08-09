package com.loopers.infrastructure.catalog.query;

import com.loopers.domain.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findById(Long id);
}
