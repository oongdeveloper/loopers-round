package com.loopers.domain.like;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LikeRepository {
    void save(Like entity);
    Optional<Like> findById(Like.LikeId id);
    boolean existsById(Like.LikeId id);

    Page<Like> findByIdUserId(Long userId, Pageable pageable);
    List<Object[]> countLikesByProductCatalogIds(Collection<Long> productCatalogIds);

}
