package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductSku;
import com.loopers.domain.product.projections.ProductListProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductFacade {
    private final BrandService brandService;
    private final ProductService productService;

    public ProductFacade(BrandService brandService, ProductService productService) {
        this.brandService = brandService;
        this.productService = productService;
    }

    public Page<ProductInfo.DataList> getProductList(ProductQuery.Summary query) {
        Page<ProductListProjection> productList = productService.getProductList(query.brandId(), query.type().name(), query.pageable());
        return productList.map(projection ->
                new ProductInfo.DataList(
                        projection.getId(),
                        projection.getBrandName(),
                        projection.getProductName(),
                        projection.getPrice(),
                        projection.getImageUrl(),
                        projection.getDescription(),
                        projection.getPublishedAt() // 주의: 메서드 이름이 getPublishedAt()일 경우
                )
        );
    }

    public ProductInfo.DataDetail getProductDetail(ProductQuery.Detail query) {
        Product product = productService.getProductDetail(query.productId());
        Brand brand = brandService.find(product.getBrandId());

        return toDataDetail(product, brand);
    }

    private ProductInfo.DataDetail toDataDetail(Product product, Brand brand) {
        return new ProductInfo.DataDetail(
                product.getId(),
                brand.getBrandName(),
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPublishedAt(),
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
