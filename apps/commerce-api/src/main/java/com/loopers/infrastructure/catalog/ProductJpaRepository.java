package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.ProductCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductCatalog, Long> {
    List<ProductCatalog> findTop5ByBrandIdOrderByPublishedAtDesc(Long id);

    @Query("SELECT p FROM ProductCatalog p " +
            "WHERE (:#{#brandId == null} = true OR p.brandId = :brandId) " +
            "ORDER BY p.publishedAt DESC")
    List<ProductCatalog> findProductsByBrandIdOrAll(@Param("brandId") Long id);

    Optional<ProductCatalog> findById(Long id);
    Page<ProductCatalog> findAll(Pageable pageable);

    Page<ProductCatalog> findByBrandId(Long id, Pageable pageable);
    List<ProductCatalog> findByIdIn(Collection<Long> ids);
}
