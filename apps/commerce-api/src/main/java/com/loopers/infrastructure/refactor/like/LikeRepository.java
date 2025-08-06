package com.loopers.infrastructure.refactor.like;

import com.loopers.domain.like.Like;
import com.loopers.infrastructure.refactor.like.projections.LikeProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends Repository<Like, Long> {
    @Query(value = """
            SELECT
                l.ref_user_id AS userId,
                p.id AS productCatalogId,
                b.brand_name AS brandName,
                p.product_name AS productName,
                p.price AS price,
                p.image_url AS imageUrl,
                p.description AS description,
                p.published_at AS publishedAt
            FROM product_catalog p
            INNER JOIN brand_catalog b on p.ref_brand_id = b.id
            INNER JOIN likes l on l.ref_product_catalog_id = p.id
            WHERE l.ref_user_id = :userId
            AND l.deleted_at IS NULL
            ORDER BY  p.created_at END desc
            """, nativeQuery = true)
    Page<LikeProductProjection> findByUserId(@Param("userId") Long userId);
}
