package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository jpaRepository;

    public LikeRepositoryImpl(LikeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Like entity) {
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Like> findById(Like.LikeId id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(Like.LikeId id) {
        return false;
    }

    @Override
    public Page<Like> findByIdUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Object[]> countLikesByProductCatalogIds(Collection<Long> productCatalogIds) {
        return List.of();
    }
}
