package com.loopers.domain.like;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository {
    List<ProductLike> getProductLikes(Collection<Long> productIds);

    Optional<ProductLike> getProductLike(Long productId);
}
