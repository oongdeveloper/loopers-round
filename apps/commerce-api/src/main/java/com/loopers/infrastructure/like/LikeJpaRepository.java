package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Like.LikeId> {
    Optional<Like> findById(Like.LikeId id); // JpaRepository 기본 제공
    boolean existsById(Like.LikeId id); // JpaRepository 기본 제공
    Page<Like> findByIdUserId(Long userId, Pageable pageable);
    Long countByIdProductCatalogId(Long productCatalogId);

    @Query("SELECT pl.id.productCatalogId FROM Like pl WHERE pl.id.userId = :userId")
    Page<Long> findProductCatalogIdsByUserId(@Param("userId") Long userId, Pageable pageable);
}
