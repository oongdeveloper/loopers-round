package com.loopers.application.catalog;

import com.loopers.domain.catalog.BrandCatalogService;
import com.loopers.domain.catalog.ProductCatalogService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductSkuService;
import org.springframework.stereotype.Service;

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
//
//    public ProductCatalogDetailInfo retrieveProductDetail(Long productId){
//        Product product = productCatalogService.findById(productId)
//                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));
//
//        Brand brand = brandCatalogService.find(product.getBrandId());
//
//        List<ProductSku> skus = productSkuService.findByProductCatalogId(productId);
//        List<ProductCatalogDetailInfo.SkuInfo> skuInfos = convertSkuToSkuInfos(skus);
//
//        return new ProductCatalogDetailInfo(
//                product.getId(),
//                brand.getBrandName(),
//                product.getProductName(),
//                product.getPrice(),
//                product.getImageUrl(),
//                product.getDescription(),
//                product.getPublishedAt(),
//                skuInfos
//        );
//    }
//
//    public Page<LikeProductDetailInfo> getLikedProductDetails(Long userId, LikeV1Dto.LikeRequest request){
//        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy().getPropertyName());
//        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
//
//        Page<Like> likesPage = likeService.findByIdUserId(userId, pageable);
//
//        List<Long> productCatalogIds = likesPage.getContent().stream()
//                .map(like -> like.getId().getProductCatalogId())
//                .collect(Collectors.toList());
//
//        List<Product> products = productCatalogService.findByIds(productCatalogIds);
//
//        Map<Long, Product> productCatalogMap = products.stream()
//                .collect(Collectors.toMap(Product::getId, productCatalog -> productCatalog));
//
//        List<LikeProductDetailInfo> likedProductDetails = likesPage.getContent().stream()
//                .map(like -> {
//                    Product product = productCatalogMap.get(like.getId().getProductCatalogId());
//                    return new LikeProductDetailInfo(like, product);
//                })
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(
//                likedProductDetails,
//                pageable,
//                likesPage.getTotalElements()
//        );
//    }
//
//
//    public Page<ProductCatalogInfo> retrieveProductCatalog(Optional<Long> brandId, ProductCatalogV1Dto.ProductCatalogRequest request){
//        Sort sort = Sort.by(Sort.Direction.fromString(request.direction().name()), request.sortBy().getPropertyName());
//        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);
//
//        brandId.ifPresent(id -> brandCatalogService.find(id));
//        Page<Product> productCatalogs = productCatalogService.find(brandId, pageable);
//        return convertToProductCatalogInfoPage(productCatalogs);
//    }
//
//    private Page<ProductCatalogInfo> convertToProductCatalogInfoPage(Page<Product> productCatalogsPage) {
//        List<Product> products = productCatalogsPage.getContent();
//
//        Set<Long> brandIds = products.stream()
//                .map(Product::getBrandId)
//                .collect(Collectors.toSet());
//
//        List<Brand> brands = brandCatalogService.findAllByIds(brandIds);
//
//        Map<Long, String> brandIdToNameMap = brands.stream()
//                .collect(Collectors.toMap(Brand::getId, Brand::getBrandName));
//
//        return productCatalogsPage.map(productCatalog -> {
//            String brandName = brandIdToNameMap.getOrDefault(
//                    productCatalog.getBrandId(), "Unknown Brand");
//
//            return ProductCatalogInfo.from(productCatalog, brandName);
//        });
//    }
//
//    private List<ProductCatalogDetailInfo.SkuInfo> convertSkuToSkuInfos(List<ProductSku> skus) {
//        List<ProductCatalogDetailInfo.SkuInfo> skuInfos = skus.stream().map(sku -> {
//            List<SkuOption> skuOptions = sku.getSkuOptions();
//
//            List<ProductCatalogDetailInfo.OptionDetail> optionDetails = skuOptions.stream()
//                    .map(so -> new ProductCatalogDetailInfo.OptionDetail(
//                            so.getOptionName().getName(),
//                            so.getOptionValue().getValue()
//                    ))
//                    .collect(Collectors.toList());
//
//            return new ProductCatalogDetailInfo.SkuInfo(
//                    sku.getId(),
//                    sku.getUnitPrice(),
//                    sku.getImageUrl(),
//                    sku.getStatus().getCode(),
//                    optionDetails
//            );
//        }).collect(Collectors.toList());
//        return skuInfos;
//    }
}
