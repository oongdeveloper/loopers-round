package com.loopers.domain.like;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void save(Long userId, Long productCatalogId) {
        likeRepository.save(Like.of(userId, productCatalogId));
    }

    public Optional<Like> find(Long userId, Long productCatalogId) {
        return likeRepository.findById(Like.LikeId.of(userId, productCatalogId));
    }

    public boolean existsById(Long userId, Long productCatalogId){
        return likeRepository.existsById(Like.LikeId.of(userId, productCatalogId));
    }

    public Page<Like> findByIdUserId(Long userId, Pageable pageable){
        return likeRepository.findByIdUserId(userId, pageable);
    }

}
