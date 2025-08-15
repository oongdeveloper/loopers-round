package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.projections.LikeProductProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
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

    public Page<LikeResult.DataList> getLikeProductList(LikeQuery.Summary query) {
        Page<LikeProductProjection> likeProductList = likeService.findByUserId(query.userId(), query.pageable());
        return likeProductList.map(projection ->
                new LikeResult.DataList(
                        projection.getUserId(),
                        projection.getProductCatalogId(),
                        projection.getBrandName(),
                        projection.getProductName(),
                        projection.getPrice(),
                        projection.getImageUrl(),
                        projection.getDescription(),
                        projection.getPublishedAt()
                )
        );
    }
}
