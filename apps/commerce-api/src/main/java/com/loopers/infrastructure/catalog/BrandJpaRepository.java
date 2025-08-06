package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllById(Iterable<Long> ids);
}
