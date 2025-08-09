package com.loopers.infrastructure.like.query;

import com.loopers.application.like.query.LikeResult;
import com.loopers.application.like.query.LikeQuery;
import com.loopers.application.like.query.LikeQueryFacade;
import com.loopers.infrastructure.like.query.projections.LikeProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class LikeQueryFacadeImpl implements LikeQueryFacade {
    private final LikeQueryRepository likeQueryRepository;

    public LikeQueryFacadeImpl(LikeQueryRepository likeQueryRepository) {
        this.likeQueryRepository = likeQueryRepository;
    }

    @Override
    public Page<LikeResult.DataList> getLikeProductList(LikeQuery.Summary query) {
        Page<LikeProductProjection> likeProductList = likeQueryRepository.findByUserId(query.userId(), query.pageable());
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
