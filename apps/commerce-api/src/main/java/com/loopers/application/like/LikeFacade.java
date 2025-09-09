package com.loopers.application.like;

import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.ProductLikeService;
import com.loopers.domain.like.projections.LikeProductProjection;
import com.loopers.event.producer.GlobalEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class LikeFacade {
    private final LikeService likeService;
    private final ProductLikeService productLikeService;
    private final DomainEventPublisher eventPublisher;
    private final GlobalEventPublisher globalEventPublisher;

    public LikeFacade(LikeService likeService, ProductLikeService productLikeService, DomainEventPublisher eventPublisher, GlobalEventPublisher globalEventPublisher) {
        this.likeService = likeService;
        this.productLikeService = productLikeService;
        this.eventPublisher = eventPublisher;
        this.globalEventPublisher = globalEventPublisher;
    }

    @Transactional
    public void like(Long userId, Long productCataglogId){
        likeService.find(userId, productCataglogId)
                .ifPresentOrElse(
                        like -> {
                            like.restore();
                            eventPublisher.publish(like.pullDomainEvents());
                        },
                        () -> {
                            likeService.save(userId, productCataglogId);
                        }
                );
        globalEventPublisher.publish(LikeAppEvent.Liked.of(userId, productCataglogId));
    }

    @Transactional
    public void unlike(Long userId, Long productCataglogId){
        likeService.find(userId, productCataglogId)
                .ifPresent(
                        like -> {
                            like.delete();
                            eventPublisher.publish(like.pullDomainEvents());
                        }
                );
        globalEventPublisher.publish(LikeAppEvent.UnLiked.of(userId, productCataglogId));
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

    public void increseLikeCount(Long productId){
        productLikeService.increase(productId);
    }

    public void decreseLikeCount(Long productId){
        productLikeService.decrease(productId);
    }
}
