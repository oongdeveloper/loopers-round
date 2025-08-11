package com.loopers.infrastructure.catalog;

import com.loopers.domain.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findTop5ByBrandIdOrderByPublishedAtDesc(Long id);

    @Query("SELECT p FROM Product p " +
            "WHERE (:#{#brandId == null} = true OR p.brandId = :brandId) " +
            "ORDER BY p.publishedAt DESC")
    List<Product> findProductsByBrandIdOrAll(@Param("brandId") Long id);

    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByBrandId(Long id, Pageable pageable);
    List<Product> findByIdIn(Collection<Long> ids);
}
