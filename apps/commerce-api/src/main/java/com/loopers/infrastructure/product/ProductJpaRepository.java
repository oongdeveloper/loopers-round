package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.projections.ProductListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT
                p.id AS id,
                b.brand_name AS brandName,
                p.product_name AS productName,
                p.price AS price,
                p.image_url AS imageUrl,
                p.description AS description,
                p.published_at AS publishedAt,
                l.likes_count
            FROM product_catalog p
            INNER JOIN brand_catalog b on p.brand_id = b.id
            INNER JOIN (
            	SELECT ref_product_id, count(1) as likes_count
            	from likes
            	group by ref_product_id
            ) l on l.ref_product_id = p.id
            WHERE (:brandId IS NULL OR p.brandId = :brandId)
            ORDER BY
                CASE WHEN :sort = 'LATEST' THEN p.created_at END desc,
                CASE WHEN :sort = 'PRICE_ASC' THEN p.price END asc
                CASE WHEN :sort = 'LIKES_DESC' THEN l.likes_count END desc  
            """, nativeQuery = true)
        // TODO. ProductCatalog 는 현재 "상태" 가 없음.
    Page<ProductListProjection> findByBrandId(@Param("brandId") Long brandId,
                                              @Param("sort") String sort,
                                              Pageable pageable);

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "LEFT JOIN FETCH p.skus s " +
            "LEFT JOIN FETCH s.skuOptions so " +
            "LEFT JOIN FETCH so.optionName on " +
            "LEFT JOIN FETCH so.optionValue ov " +
            "WHERE p.id = :productId")
    Optional<Product> findById(@Param("productId") Long productId);

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "LEFT JOIN FETCH p.skus s " +
            "LEFT JOIN FETCH s.skuOptions so " +
            "LEFT JOIN FETCH so.optionName on " +
            "LEFT JOIN FETCH so.optionValue ov " +
            "WHERE s.id IN :skuIds")
    List<Product> findProductsBySkuIds(@Param("skuIds") Collection<Long> skuIds);
}
