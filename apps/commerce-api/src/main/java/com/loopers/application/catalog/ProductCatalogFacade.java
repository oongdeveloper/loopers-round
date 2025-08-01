package com.loopers.application.catalog;

import com.loopers.application.like.LikeProductDetailInfo;
import com.loopers.domain.catalog.BrandCatalog;
import com.loopers.domain.catalog.BrandCatalogService;
import com.loopers.domain.catalog.ProductCatalog;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.ProductSkuService;
import com.loopers.domain.product.SkuOption;
import com.loopers.interfaces.api.catalog.ProductCatalogV1Dto;
import com.loopers.interfaces.api.like.LikeV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductCatalogFacade {
    private final BrandCatalogService brandCatalogService;
    private final ProductCatalogService productCatalogService;
    private final ProductSkuService productSkuService;
    private final LikeService likeService;

    public ProductCatalogFacade(BrandCatalogService brandCatalogService, ProductCatalogService productCatalogService, ProductSkuService productSkuService, LikeService likeService) {
        this.brandCatalogService = brandCatalogService;
        this.productCatalogService = productCatalogService;
        this.productSkuService = productSkuService;
        this.likeService = likeService;
    }

    public ProductCatalogDetailInfo retrieveProductDetail(Long productId){
        ProductCatalog productCatalog = productCatalogService.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

        BrandCatalog brandCatalog = brandCatalogService.find(productCatalog.getBrandId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "브랜드를 찾을 수 없습니다."));

        List<ProductSku> skus = productSkuService.findByProductCatalogId(productId);
        List<ProductCatalogDetailInfo.SkuInfo> skuInfos = convertSkuToSkuInfos(skus);

        return new ProductCatalogDetailInfo(
                productCatalog.getId(),
                brandCatalog.getBrandName(),
                productCatalog.getProductName(),
                productCatalog.getBasePrice(),
                productCatalog.getImageUrl(),
                productCatalog.getDescription(),
                productCatalog.getPublishedAt(),
                skuInfos
        );
    }

    public Page<LikeProductDetailInfo> getLikedProductDetails(Long userId, LikeV1Dto.LikeRequest request){
        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy().getPropertyName());
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);

        Page<Like> likesPage = likeService.findByIdUserId(userId, pageable);

        List<Long> productCatalogIds = likesPage.getContent().stream()
                .map(like -> like.getId().getProductCatalogId())
                .collect(Collectors.toList());

        List<ProductCatalog> productCatalogs = productCatalogService.findByIds(productCatalogIds);

        Map<Long, ProductCatalog> productCatalogMap = productCatalogs.stream()
                .collect(Collectors.toMap(ProductCatalog::getId, productCatalog -> productCatalog));

        List<LikeProductDetailInfo> likedProductDetails = likesPage.getContent().stream()
                .map(like -> {
                    ProductCatalog productCatalog = productCatalogMap.get(like.getId().getProductCatalogId());
                    return new LikeProductDetailInfo(like, productCatalog);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(
                likedProductDetails,
                pageable,
                likesPage.getTotalElements()
        );
    }


    public Page<ProductCatalogInfo> retrieveProductCatalog(Optional<Long> brandId, ProductCatalogV1Dto.ProductCatalogRequest request){
        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy().getPropertyName());
        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);

        brandId.ifPresent(id -> brandCatalogService.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "Brand 를 찾을 수 없습니다.")));
        Page<ProductCatalog> productCatalogs = productCatalogService.find(brandId, pageable);
        return convertToProductCatalogInfoPage(productCatalogs);
    }

    private Page<ProductCatalogInfo> convertToProductCatalogInfoPage(Page<ProductCatalog> productCatalogsPage) {
        List<ProductCatalog> productCatalogs = productCatalogsPage.getContent();

        Set<Long> brandIds = productCatalogs.stream()
                .map(ProductCatalog::getBrandId)
                .collect(Collectors.toSet());

        List<BrandCatalog> brandCatalogs = brandCatalogService.findAllByIds(brandIds);

        Map<Long, String> brandIdToNameMap = brandCatalogs.stream()
                .collect(Collectors.toMap(BrandCatalog::getId, BrandCatalog::getBrandName));

        return productCatalogsPage.map(productCatalog -> {
            String brandName = brandIdToNameMap.getOrDefault(
                    productCatalog.getBrandId(), "Unknown Brand");

            return ProductCatalogInfo.from(productCatalog, brandName);
        });
    }

    private List<ProductCatalogDetailInfo.SkuInfo> convertSkuToSkuInfos(List<ProductSku> skus) {
        List<ProductCatalogDetailInfo.SkuInfo> skuInfos = skus.stream().map(sku -> {
            List<SkuOption> skuOptions = sku.getSkuOptions();

            List<ProductCatalogDetailInfo.OptionDetail> optionDetails = skuOptions.stream()
                    .map(so -> new ProductCatalogDetailInfo.OptionDetail(
                            so.getOptionName().getName(),
                            so.getOptionValue().getValue()
                    ))
                    .collect(Collectors.toList());

            return new ProductCatalogDetailInfo.SkuInfo(
                    sku.getId(),
                    sku.getUnitPrice(),
                    sku.getImageUrl(),
                    sku.getStatus().getCode(),
                    optionDetails
            );
        }).collect(Collectors.toList());
        return skuInfos;
    }

}
