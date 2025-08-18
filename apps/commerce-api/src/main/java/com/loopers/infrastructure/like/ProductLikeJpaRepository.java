package com.loopers.infrastructure.like;

import com.loopers.domain.like.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {
    List<ProductLike> findByProductIdIn(Collection<Long> productIds);
    Optional<ProductLike> findById(Long productId);
}
