package com.loopers.application.refactor.like;

import org.springframework.data.domain.Page;

public interface LikeQueryFacade {
    Page<LikeInfo.DataList> getLikeProductList(LikeQuery.ListQuery query);
}
