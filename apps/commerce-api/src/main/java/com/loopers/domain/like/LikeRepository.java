package com.loopers.domain.like;

import com.loopers.domain.like.projections.LikeProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    void save(Like entity);
    Optional<Like> findById(Like.LikeId id);
    boolean existsById(Like.LikeId id);

    Page<Like> findByIdUserId(Long userId, Pageable pageable);
    List<Object[]> countLikesByProductCatalogIds(Collection<Long> productCatalogIds);

    int countBy(Long userId, Long productId);

    int countByProductId(Long productId);

    Page<LikeProductProjection> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
