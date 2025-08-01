package com.loopers.domain.catalog;


import com.loopers.application.catalog.BrandCatalogFacade;
import com.loopers.env.IntegrationTest;
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
public class BrandCatalogIntegrationTest {

    private final BrandCatalogFacade brandCatalogFacade;
    private final BrandRepository brandRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public BrandCatalogIntegrationTest(BrandCatalogFacade brandCatalogFacade, BrandRepository brandRepository, DatabaseCleanUp databaseCleanUp) {
        this.brandCatalogFacade = brandCatalogFacade;
        this.brandRepository = brandRepository;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("브랜드조회")
    @Nested
    class RetrieveBrandCatalog {
        @Test
        @DisplayName("브랜드 정보가 존재하지 않으면 Not Found 오류가 발생합니다.")
        void returnNotFount_whenBrandNotFound(){
            // arrange
            BrandCatalog appleBrandCatalog = BrandCatalog.from("Apple", "https://example.com/logos/apple.png");
            BrandCatalog samsungBrandCatalog = BrandCatalog.from("Samsung", "https://example.com/logos/samsung.png");
            BrandCatalog lgBrandCatalog = BrandCatalog.from("LG", "https://example.com/logos/lg.png");

            brandRepository.save(appleBrandCatalog);
            brandRepository.save(samsungBrandCatalog);
            brandRepository.save(lgBrandCatalog);

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                brandCatalogFacade.getBrandDetailWithProducts(4L);
            });
            // asserts
            Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

}
