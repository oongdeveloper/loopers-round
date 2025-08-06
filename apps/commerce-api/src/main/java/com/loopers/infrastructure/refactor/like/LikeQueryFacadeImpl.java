package com.loopers.infrastructure.refactor.like;

import com.loopers.application.refactor.like.LikeInfo;
import com.loopers.application.refactor.like.LikeQuery;
import com.loopers.application.refactor.like.LikeQueryFacade;
import com.loopers.infrastructure.refactor.like.projections.LikeProductProjection;
import org.springframework.data.domain.Page;

public class LikeQueryFacadeImpl implements LikeQueryFacade {
    private final LikeRepository likeRepository;

    public LikeQueryFacadeImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Page<LikeInfo.DataList> getLikeProductList(LikeQuery.ListQuery query) {
        Page<LikeProductProjection> likeProductList = likeRepository.findByUserId(query.userId());
        return likeProductList.map(projection ->
            new LikeInfo.DataList(
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
