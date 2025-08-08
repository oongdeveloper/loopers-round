package com.loopers.infrastructure.catalog.query;

import com.loopers.application.catalog.query.ProductInfo;
import com.loopers.application.catalog.query.ProductQuery;
import com.loopers.application.catalog.query.ProductQueryFacade;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.product.ProductSku;
import com.loopers.infrastructure.catalog.query.projections.ProductListProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductQueryFacadeImpl implements ProductQueryFacade {
    private final ProductQueryRepository productQueryRepository;
    private final ProductSkuQueryRepository productSkuQueryRepository;
    private final BrandRepository brandRepository;

    public ProductQueryFacadeImpl(ProductQueryRepository productQueryRepository, ProductSkuQueryRepository productSkuQueryRepository, BrandRepository brandRepository) {
        this.productQueryRepository = productQueryRepository;
        this.productSkuQueryRepository = productSkuQueryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public Page<ProductInfo.DataList> getProductList(ProductQuery.Summary query) {
        Page<ProductListProjection> productList = productQueryRepository.findByBrandId(query.brandId(), query.type().name(), query.pageable());
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

    @Override
    public ProductInfo.DataDetail getProductDetail(ProductQuery.Detail query) {
        Product product = productQueryRepository.findById(query.productId())
                                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 정보입니다."));

        Brand brand = brandRepository.findById(product.getBrandId())
                                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드 입니다."));
        List<ProductSku> skus = productSkuQueryRepository.findById(query.productId());

        return toDataDetail(product, brand, skus);
    }

    private ProductInfo.DataDetail toDataDetail(Product product, Brand brand, List<ProductSku> skus) {
        String brandName = brand.getBrandName();
        String productName = product.getProductName();
        BigDecimal price = product.getPrice();
        String imageUrl = product.getImageUrl();
        String description = product.getDescription();
        ZonedDateTime publishedAt = product.getPublishedAt();

        List<ProductInfo.SkuInfo> skuInfos = skus.stream()
                .map(this::toSkuInfo)
                .collect(Collectors.toList());

        return new ProductInfo.DataDetail(
                product.getId(),
                brandName,
                productName,
                price,
                imageUrl,
                description,
                publishedAt,
                skuInfos
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
