package com.loopers.infrastructure.catalog.query;

import com.loopers.domain.catalog.Product;
import com.loopers.infrastructure.catalog.query.projections.ProductListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductQueryRepository extends Repository<Product, Long> {
    @Query(value = """
            SELECT
                p.id AS id,
                b.brand_name AS brandName,
                p.product_name AS productName,
                p.price AS price,
                p.image_url AS imageUrl,
                p.description AS description,
                p.published_at AS publishedAt
            FROM product_catalog p
            INNER JOIN brand_catalog b on p.brand_id = b.id
            WHERE (:brandId IS NULL OR p.brandId = :brandId)
            ORDER BY
                CASE WHEN :sort = 'LATEST' THEN p.created_at END desc,
                CASE WHEN :sort = 'PRICE_ASC' THEN p.price END asc 
            """, nativeQuery = true)
    // TODO. ProductCatalog 는 현재 "상태" 가 없음.
    Page<ProductListProjection> findByBrandId(@Param("brandId") Long brandId,
                                              @Param("sort") String sort,
                                              Pageable pageable);

    Optional<Product> findById(Long productId);
}
