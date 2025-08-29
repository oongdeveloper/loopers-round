package com.loopers.domain.like;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductLikeService {
    private final ProductLikeRepository productLikeRepository;

    public ProductLikeService(ProductLikeRepository productLikeRepository) {
        this.productLikeRepository = productLikeRepository;
    }

    public List<ProductLike> getList(Collection<Long> productIds) {
        return productLikeRepository.getProductLikes(productIds);
    }

    public ProductLike get(Long productId) {
        return productLikeRepository.getProductLike(productId)
                .orElse(ProductLike.of(productId, 0L));
    }

    public void increase(Long productId){
        productLikeRepository.getProductLike(productId)
                .ifPresent(ProductLike::like);
    }

    public void decrease(Long productId){
        productLikeRepository.getProductLike(productId)
                .ifPresent(ProductLike::unlike);
    }

}
