package com.loopers.domain.product;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.domain.product.projections.ProductListProjectionV3;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final RedisCacheWrapper redisCacheWrapper;
    private final String PRODUCT_PAGE_REDIS_PREFIX = "product:page:";
    private final String PRODUCT_COUNT_REDIS_PREFIX = "product:count";

    public ProductService(ProductRepository productRepository, ProductSkuRepository productSkuRepository, RedisCacheWrapper redisCacheWrapper) {
        this.productRepository = productRepository;
        this.productSkuRepository = productSkuRepository;
        this.redisCacheWrapper = redisCacheWrapper;
    }

    public Page<ProductListProjectionV2> findByBrandIdBySortType(Long brandId, String sort, Pageable pageable){
        Long count = redisCacheWrapper.get(PRODUCT_COUNT_REDIS_PREFIX, Long.class);
        if (count == null) {
            count = productRepository.count();
            redisCacheWrapper.set(PRODUCT_COUNT_REDIS_PREFIX, count, 10L, TimeUnit.MINUTES);
        }
        List<ProductListProjectionV2> content = productRepository.findByBrandIdBySortType(brandId, sort, pageable);
        return new PageImpl<>(content, pageable, count);
    };

    public List<ProductListProjectionV3> getProductListV3(Long brandId,
                                                          String sort,
                                                          Pageable pageable) {
        return productRepository.findByBrandIdV3(brandId, sort, pageable);
    }

    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품입니다."));
    }

    public List<Product> getProuctListByIds(Collection<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return List.of();
        }
        return productRepository.findProductsBySkuIds(skuIds);
    }
}
