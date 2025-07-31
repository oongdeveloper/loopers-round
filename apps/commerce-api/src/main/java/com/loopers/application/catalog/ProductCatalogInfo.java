package com.loopers.application.catalog;

import com.loopers.domain.catalog.ProductCatalog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ProductCatalogInfo (
        String brandName,
        String productName,
        BigDecimal basePrice,
        String imageUrl,
        String description
){
    public static ProductCatalogInfo from(ProductCatalog product, String brandName){
        return new ProductCatalogInfo(
                brandName,
                product.getProductName(),
                product.getBasePrice(),
                product.getImageUrl(),
                product.getDescription()
        );
    }

    public static List<ProductCatalogInfo> from(List<ProductCatalog> productCatalogs, Map<Long, String> brandIdToNameMap) {
        if (productCatalogs == null) {
            return List.of();
        }
        return productCatalogs.stream()
                .map(product -> {
                    // ProductCatalog의 brandId를 이용하여 brandName을 Map에서 찾음
                    String brandName = brandIdToNameMap.getOrDefault(product.getBrandId(), "Unknown Brand"); // getBrandId() 가정
                    return ProductCatalogInfo.from(product, brandName); // 단일 브랜드 이름을 리스트로
                })
                .collect(Collectors.toList());
    }
}
