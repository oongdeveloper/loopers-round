package com.loopers.application.like.query;

import org.springframework.data.domain.Page;

public interface LikeQueryFacade {
    Page<LikeResult.DataList> getLikeProductList(LikeQuery.Summary query);
}
