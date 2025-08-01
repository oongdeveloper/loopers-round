package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.BrandCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandJpaRepository extends JpaRepository<BrandCatalog, Long> {
    List<BrandCatalog> findAllById(Iterable<Long> ids);
}
