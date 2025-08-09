package com.loopers.domain.catalog;


import com.loopers.application.catalog.query.BrandQuery;
import com.loopers.application.catalog.query.BrandQueryFacade;
import com.loopers.env.IntegrationTest;
import com.loopers.infrastructure.catalog.query.BrandRepository;
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

    private final BrandRepository brandRepository;
    private final DatabaseCleanUp databaseCleanUp;

    private final BrandQueryFacade brandQueryFacade;

    @Autowired
    public BrandIntegrationTest(BrandRepository brandRepository, DatabaseCleanUp databaseCleanUp, BrandQueryFacade brandQueryFacade) {
        this.brandRepository = brandRepository;
        this.databaseCleanUp = databaseCleanUp;
        this.brandQueryFacade = brandQueryFacade;
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

            brandRepository.save(appleBrand);
            brandRepository.save(samsungBrand);
            brandRepository.save(lgBrand);
            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                brandQueryFacade.getBrandDetail(
                        BrandQuery.Detail.of(4L)
                );
            });
            // asserts
            Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

}
