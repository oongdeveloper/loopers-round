package com.loopers.domain.catalog;

import com.loopers.env.UnitTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@UnitTest
class BrandTest {

    @Test
    @DisplayName("브랜드Name 이 100자가 넘는 경우, BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenBrandIdNotFound(){
        String brandName = "이렇게 긴 문장의 브랜드 네임은 허용되지 않습니다.".repeat(20);
        String logoImg = "https://loppers-ecommerce/cdn/images/brand/1";
        CoreException exception = assertThrows(CoreException.class, () -> {
            Brand.from(brandName, logoImg);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @Test
    @DisplayName("URL 형식이 맞지 않는 경우, BAD_REQUEST 오류를 반환한다.")
    void returnBadRequest_whenMalformedURL(){
        String brandName = "loopers";
        String logoImg = "aa:bb:cc";
        CoreException exception = assertThrows(CoreException.class, () -> {
            Brand.from(brandName, logoImg);
        });
        Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

}
