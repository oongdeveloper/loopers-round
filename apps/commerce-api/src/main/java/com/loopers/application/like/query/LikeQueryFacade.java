package com.loopers.application.like.query;

import com.loopers.application.like.LikeQuery;
import com.loopers.application.like.LikeResult;
import org.springframework.data.domain.Page;

public interface LikeQueryFacade {
    Page<LikeResult.DataList> getLikeProductList(LikeQuery.Summary query);
}
