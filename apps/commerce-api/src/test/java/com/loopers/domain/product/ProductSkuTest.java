package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductSkuTest {

    @Test
    @DisplayName("상품 옵션 가격은 0 이하이거나 10억 이상이면 BAD_REQUST 를 반환한다.")
    void returnBadRequest_whenOptionPriceOutOfRange(){
        BigDecimal zeroPrice = BigDecimal.valueOf(0L);
        BigDecimal minusPrice = BigDecimal.valueOf(-1000L);
        BigDecimal maxPrice = BigDecimal.valueOf(10_000_000_000L);
        String imageUrl = "https://loppers-ecommerce/cdn/images/product/1";


        CoreException zeroPriceException = assertThrows(CoreException.class, () -> {
            ProductSku.from(List.of(), imageUrl, zeroPrice, ProductSku.SkuStatus.AVAILABLE, null);
        });
        CoreException minusPriceException = assertThrows(CoreException.class, () -> {
            ProductSku.from(List.of(), imageUrl, minusPrice, ProductSku.SkuStatus.AVAILABLE, null);
        });
        CoreException maxPriceException = assertThrows(CoreException.class, () -> {
            ProductSku.from(List.of(), imageUrl, maxPrice, ProductSku.SkuStatus.AVAILABLE, null);
        });
        Assertions.assertThat(zeroPriceException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        Assertions.assertThat(minusPriceException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        Assertions.assertThat(maxPriceException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품 이미지 URL 이 URL 형식이 아니면 BAD_REQUST 를 반환한다.")
    void returnBadRequest_whenMalformedURL(){
        BigDecimal price = BigDecimal.valueOf(10000L);
        String imageUrl = "haa.aa.aa";

        CoreException exception = assertThrows(CoreException.class, () -> {
            ProductSku.from(List.of(), imageUrl, price, ProductSku.SkuStatus.AVAILABLE, null);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품 옵션의 값이 100자 이상이면 BAD_REQUST 를 반환한다.")
    void returnBadRequest_whenTooLongOptionValue(){
        String value = "옵션 값 value".repeat(100);

        CoreException exception = assertThrows(CoreException.class, () -> {
            OptionValue.from(value);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품 옵션 Name이 50자 이상이면 BAD_REQUST 를 반환한다.")
    void returnBadRequest_whenTooLongOptionName(){
        String value = "옵션 Name".repeat(100);
        String description = "옵션 Name 설명입니다.";

        CoreException exception = assertThrows(CoreException.class, () -> {
            OptionName.from(value, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품 옵션 설명이 250자 이상이면 BAD_REQUST 를 반환한다.")
    void returnBadRequest_whenTooLongOptionNameDescription(){
        String value = "옵션 Name";
        String description = "옵션 Name 설명입니다.".repeat(100);

        CoreException exception = assertThrows(CoreException.class, () -> {
            OptionName.from(value, description);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

}
