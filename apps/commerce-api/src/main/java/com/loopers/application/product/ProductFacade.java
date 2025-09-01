package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.ProductLike;
import com.loopers.domain.like.ProductLikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductFacade {
    private final BrandService brandService;
    private final ProductService productService;
    private final ProductLikeService productLikeService;
    private final ApplicationEventPublisher eventPublisher;

    public ProductFacade(BrandService brandService, ProductService productService, ProductLikeService productLikeService, ApplicationEventPublisher eventPublisher) {
        this.brandService = brandService;
        this.productService = productService;
        this.productLikeService = productLikeService;
        this.eventPublisher = eventPublisher;
    }

    public Page<ProductInfo.DataList> getProductList(ProductQuery.Summary query) {
        if (query.brandId() != null) brandService.get(query.brandId());
        Page<ProductListProjectionV2> productList = productService.findByBrandIdBySortType(query.brandId(), query.type().name(), query.pageable());
        return productList.map(pj ->
                new ProductInfo.DataList(
                        pj.getId(),
                        pj.getBrandName(),
                        pj.getProductName(),
                        pj.getPrice(),
                        pj.getImageUrl(),
                        pj.getDescription(),
                        pj.getPublishedAt(),
                        pj.getLikeCount()
                )
        );
    }

    public ProductInfo.DataDetail getProductDetail(@RequestHeader("X-USER-ID") Long userId, ProductQuery.Detail query) {
        Product product = productService.getProductDetail(query.productId());
        Brand brand = brandService.get(product.getBrandId());
        ProductLike productLike = productLikeService.get(query.productId());

        eventPublisher.publishEvent(ProductAppEvent.Clicked.of(userId, query.productId()));
        return toDataDetail(product, brand, productLike.getLikeCount());
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
