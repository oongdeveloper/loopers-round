package com.loopers.domain.catalog;

import com.loopers.env.UnitTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;


@UnitTest
class ProductCatalogTest {

    @Test
    @DisplayName("Brand ID 가 없으면 BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenBrandIdNull(){
        Long brandId = null;
        String productName = "loopers_product_1";
        BigDecimal price = BigDecimal.valueOf(10000L);
        String imageUrl = "https://loppers-ecommerce/cdn/images/product/1";
        String description = "";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductCatalog.from(brandId, productName, price, imageUrl, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품의 이름이 250자를 넘으면 BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenProductNameOver250(){
        Long brandId = 0L;
        String productName = "상품 이름이 이렇게 길면 안돼요.".repeat(30);
        BigDecimal price = BigDecimal.valueOf(10000L);
        String imageUrl = "https://loppers-ecommerce/cdn/images/product/1";
        String description = "";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductCatalog.from(brandId, productName, price, imageUrl, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품의 가격이 10억을 넘으면 BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenPriceOverThanBillion(){
        Long brandId = 0L;
        String productName = "loopers_product_1";
        BigDecimal price = BigDecimal.valueOf(10_000_000_000L);
        String imageUrl = "https://loppers-ecommerce/cdn/images/product/1";
        String description = "";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductCatalog.from(brandId, productName, price, imageUrl, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품의 가격이 0 이하면 BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenPriceLessThanZero(){
        Long brandId = null;
        String productName = "loopers_product_1";
        BigDecimal price = BigDecimal.valueOf(0L);
        String imageUrl = "https://loppers-ecommerce/cdn/images/product/1";
        String description = "";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductCatalog.from(brandId, productName, price, imageUrl, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("Image URL 이 올바른 형식이 아니면 BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenMalformedURL(){
        Long brandId = null;
        String productName = "loopers_product_1";
        BigDecimal price = BigDecimal.valueOf(10000L);
        String imageUrl = "loopers/product/1";
        String description = "";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductCatalog.from(brandId, productName, price, imageUrl, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
}
