package com.loopers.infrastructure.catalog.query;

import com.loopers.application.catalog.query.ProductProjection;
import com.loopers.application.catalog.query.ProductQuery;
import com.loopers.domain.catalog.ProductCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductCatalogJpaRepository extends JpaRepository<ProductCatalog, Long> {
    @Query(value = """
        SELECT 
            p.id AS id,
            p.productName AS name,
            p.basePrice AS basePrice,
            p.imageUrl AS imageUrl,
            p.created_at AS createdAt,
            (
                SELECT COUNT(l.id)
                FROM likes l
                WHERE l.ref_product_catalog_id = p.id
            ) AS likeCount,
        FROM product_catalog p
        WHERE (:#{#query.brandId} IS NULL OR p.brand_id = :#{#query.brandId})
        ORDER BY 
            CASE WHEN :#{#query.sort} = 'RECENT' THEN p.created_at END DESC,
            CASE WHEN :#{#query.sort} = 'LOW_PRICE' THEN minPrice END ASC,
            CASE WHEN :#{#query.sort} = 'LIKE' THEN likeCount END DESC
        """, nativeQuery = true)
    Page<ProductProjection> findProductList(ProductQuery query, Pageable pageable);
}
