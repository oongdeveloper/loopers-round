package com.loopers.infrastructure.like;

import com.loopers.domain.like.ProductLike;
import com.loopers.domain.like.ProductLikeRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductLikeRepositoryImpl implements ProductLikeRepository {
    private final ProductLikeJpaRepository jpaRepository;

    public ProductLikeRepositoryImpl(ProductLikeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ProductLike> getProductLikes(Collection<Long> productIds) {
        return jpaRepository.findByProductIdIn(productIds);
    }

    @Override
    public Optional<ProductLike> getProductLike(Long productId) {
        return Optional.empty();
    }
}
