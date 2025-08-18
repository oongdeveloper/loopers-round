package com.loopers.domain.catalog;


import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandQuery;
import com.loopers.domain.brand.Brand;
import com.loopers.env.IntegrationTest;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@SpringBootTest
public class BrandIntegrationTest {

    private final BrandJpaRepository brandJpaRepository;
    private final DatabaseCleanUp databaseCleanUp;

    private final BrandFacade brandFacade;

    @Autowired
    public BrandIntegrationTest(BrandJpaRepository brandJpaRepository, DatabaseCleanUp databaseCleanUp, BrandFacade brandFacade) {
        this.brandJpaRepository = brandJpaRepository;
        this.databaseCleanUp = databaseCleanUp;
        this.brandFacade = brandFacade;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("브랜드조회")
    @Nested
    class RetrieveBrand {
        @Test
        @DisplayName("브랜드 정보가 존재하지 않으면 Not Found 오류가 발생합니다.")
        void returnNotFound_whenBrandNotFound(){
            // arrange
            Brand appleBrand = Brand.from("Apple", "https://example.com/logos/apple.png");
            Brand samsungBrand = Brand.from("Samsung", "https://example.com/logos/samsung.png");
            Brand lgBrand = Brand.from("LG", "https://example.com/logos/lg.png");

            brandJpaRepository.save(appleBrand);
            brandJpaRepository.save(samsungBrand);
            brandJpaRepository.save(lgBrand);
            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                brandFacade.getBrandDetail(
                        BrandQuery.Detail.of(4L)
                );
            });
            // asserts
            Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

}
