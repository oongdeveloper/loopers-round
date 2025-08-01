package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LikeFacade {
    private final LikeService likeService;

    public LikeFacade(LikeService likeService) {
        this.likeService = likeService;
    }

    @Transactional
    public void like(Long userId, Long productCataglogId){
        likeService.find(userId, productCataglogId)
                .ifPresentOrElse(
                        like -> {
                            if(like.getDeletedAt() != null){
                                like.restore();
                            }
                        },
                        () -> {
                            likeService.save(userId, productCataglogId);
                        }
                );
    }

    @Transactional
    public void unlike(Long userId, Long productCataglogId){
        likeService.find(userId, productCataglogId)
                .ifPresent(
                        like -> {
                            if(like.getDeletedAt() == null){
                                like.delete();
                            }
                        }
                );
    }
}
