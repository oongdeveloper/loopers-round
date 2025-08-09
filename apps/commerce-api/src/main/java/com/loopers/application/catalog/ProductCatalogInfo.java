package com.loopers.application.catalog;

import com.loopers.domain.catalog.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ProductCatalogInfo (
        Long productCatalogId,
        String brandName,
        String productName,
        BigDecimal basePrice,
        String imageUrl,
        String description
){
    public static ProductCatalogInfo from(Product product, String brandName){
        return new ProductCatalogInfo(
                product.getId(),
                brandName,
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getDescription()
        );
    }

    public static List<ProductCatalogInfo> from(List<Product> products, Map<Long, String> brandIdToNameMap) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(product -> {
                    String brandName = brandIdToNameMap.getOrDefault(product.getBrandId(), "Unknown Brand");
                    return ProductCatalogInfo.from(product, brandName);
                })
                .collect(Collectors.toList());
    }
}
