package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.projections.LikeProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Like.LikeId> {
    Optional<Like> findById(Like.LikeId id); // JpaRepository 기본 제공
    boolean existsById(Like.LikeId id); // JpaRepository 기본 제공

    @Query("SELECT pl.id.productId FROM Like pl WHERE pl.id.userId = :userId")
    Page<Long> findProductCatalogIdsByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT count(1) FROM Like l WHERE l.id.userId = :userId AND l.id.productId = :productId")
    int countBy(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT count(1) FROM Like l WHERE l.id.productId = :productId")
    int countByProductId(@Param("productId") Long productId);


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
    Page<LikeProductProjection> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
