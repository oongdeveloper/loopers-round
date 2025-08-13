package com.loopers.domain.catalog;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductQuery;
import com.loopers.env.IntegrationTest;
import com.loopers.infrastructure.brand.BrandRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@SpringBootTest
public class ProductIntegrationTest {
    private final BrandRepository brandRepository;
    private final ProductJpaRepository productRepository;
    private final ProductFacade productFacade;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public ProductIntegrationTest(BrandRepository brandRepository, ProductJpaRepository productRepository, ProductFacade productFacade, DatabaseCleanUp databaseCleanUp) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.productFacade = productFacade;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 조회")
    @Nested
    @Sql(scripts = {"classpath:catalog/catalog-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class RetrieveProduct{

        @Test
        @DisplayName("없는 브랜드 ID 로 조회 시, Not Found 오류를 반환한다.")
        void returnNotFound_whenBrandIdNotFound(){
            assertThat(brandRepository.count()).isEqualTo(3);

            CoreException exception = assertThrows(CoreException.class, () -> {
                productFacade.getProductList(
                        ProductQuery.Summary.of(999L, "LATEST", PageRequest.of(0, 20))
                );
            });
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @Test
        @DisplayName("상품 목록 조회 시, 개수만큼 반환한다. BrandId 있을 때")
        void returnList_whenRetrieveProductWithBrandId(){
            assertThat(brandRepository.count()).isEqualTo(3);

            Page<ProductInfo.DataList> productInfos =  productFacade.getProductList(
                    ProductQuery.Summary.of(1L, "LATEST", PageRequest.of(0, 20))
            );

            assertThat(productInfos.getContent().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("상품 상세 조회 시, 존재하지 않는 상품이면 Not Found 오류를 반환한다.")
        void returnNotFound_whenProductDetailNotFound(){
            assertThat(productRepository.count()).isEqualTo(30);

            CoreException exception = assertThrows(CoreException.class, () -> {
                productFacade.getProductDetail(
                        ProductQuery.Detail.of(999L)
                );
            });
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @Test
        @DisplayName("상품 상세 조회 시, 브랜드가 존재하지 않는 상품이면 Not Found 오류를 반환한다.")
        @Sql(scripts = {"classpath:catalog/catalog-data-one-product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void returnNotFound_whenProductDetailBrandNotFound(){
            Long productIdWithNoBrand = 1L;

            CoreException exception = assertThrows(CoreException.class, () -> {
                productFacade.getProductDetail(
                        ProductQuery.Detail.of(productIdWithNoBrand)
                );
            });

            // 예외 타입 및 메시지 검증
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
            assertThat(exception.getMessage()).contains("브랜드를 찾을 수 없습니다.");
        }
    }
}
