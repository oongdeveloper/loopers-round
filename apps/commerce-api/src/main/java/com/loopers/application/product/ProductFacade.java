package com.loopers.application.product;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.ProductLike;
import com.loopers.domain.like.ProductLikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import com.loopers.event.producer.GlobalEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ProductFacade {
    private final BrandService brandService;
    private final ProductService productService;
    private final ProductLikeService productLikeService;
    private final RedisCacheWrapper redisCacheWrapper;
    private final GlobalEventPublisher globalEventPublisher;

    private final String PRODUCT_COUNT_PREFIX = "product:count";
    private final String PREFIX_PRODUCT_DETAIL = "product:detail:";

    public ProductFacade(BrandService brandService, ProductService productService, ProductLikeService productLikeService, RedisCacheWrapper redisCacheWrapper, GlobalEventPublisher globalEventPublisher) {
        this.brandService = brandService;
        this.productService = productService;
        this.productLikeService = productLikeService;
        this.redisCacheWrapper = redisCacheWrapper;
        this.globalEventPublisher = globalEventPublisher;
    }

    public Page<ProductInfo.DataList> getProductList(ProductQuery.Summary query) {
        if (query.brandId() != null) brandService.get(query.brandId());

        Long count = redisCacheWrapper.get(PRODUCT_COUNT_PREFIX, Long.class);
        if (count == null) {
            count = productService.count();
            redisCacheWrapper.set(PRODUCT_COUNT_PREFIX, count, 10L, TimeUnit.MINUTES);
        }

        List<ProductListProjectionV2> productList = productService.findByBrandIdBySortType(query.brandId(), query.type().name(), query.pageable());
        return new PageImpl<>(productList, query.pageable(), count)
                .map(pj ->
                        new ProductInfo.DataList(
                                pj.getId(),
                                pj.getBrandName(),
                                pj.getProductName(),
                                pj.getPrice(),
                                pj.getImageUrl(),
                                pj.getDescription(),
                                pj.getPublishedAt(),
                                pj.getLikeCount()
                        ));
    }

    public ProductInfo.DataDetail getProductDetail(@RequestHeader("X-USER-ID") Long userId, ProductQuery.Detail query) {
        ProductInfo.DataDetail cachedDetail = redisCacheWrapper.get(PREFIX_PRODUCT_DETAIL+query.productId(), ProductInfo.DataDetail.class);

        if (cachedDetail != null){
            globalEventPublisher.publish(ProductAppEvent.Clicked.of(userId, query.productId()));
            return cachedDetail;
        } else {
            Product product = productService.getProductDetail(query.productId());
            Brand brand = brandService.get(product.getBrandId());
            ProductLike productLike = productLikeService.get(query.productId());

            ProductInfo.DataDetail productDetail = toDataDetail(product, brand, productLike.getLikeCount());
            redisCacheWrapper.set(PREFIX_PRODUCT_DETAIL+query.productId(), productDetail, 10L, TimeUnit.MINUTES);
            globalEventPublisher.publish(ProductAppEvent.Clicked.of(userId, query.productId()));
            return productDetail;
        }
    }

    public void cacheEvict(ProductQuery.Detail query){
        redisCacheWrapper.delete(PREFIX_PRODUCT_DETAIL+query.productId());
    }

    private ProductInfo.DataDetail toDataDetail(Product product, Brand brand, Long likeCount) {
        return new ProductInfo.DataDetail(
                product.getId(),
                brand.getBrandName(),
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPublishedAt(),
                likeCount,
                product.getSkus().stream()
                        .map(this::toSkuInfo)
                        .collect(Collectors.toList())
        );
    }

    private ProductInfo.SkuInfo toSkuInfo(ProductSku sku) {
        List<ProductInfo.OptionDetail> optionDetails = sku.getSkuOptions().stream()
                .map(option -> new ProductInfo.OptionDetail(option.getOptionName().getName(), option.getOptionValue().getValue()))
                .collect(Collectors.toList());

        return new ProductInfo.SkuInfo(
                sku.getId(),
                sku.getUnitPrice(),
                sku.getImageUrl(),
                sku.getStatus().getCode(),
                optionDetails
        );
    }
}
