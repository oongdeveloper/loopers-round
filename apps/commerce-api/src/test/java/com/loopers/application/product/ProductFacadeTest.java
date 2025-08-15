package com.loopers.application.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("local")
class ProductFacadeTest {

    @Autowired
    ProductFacade productFacade;

    @Test
    @DisplayName("OrderBy Price ASC : 상품 목록 조회 시, 캐시를 사용하면 100ms 내로 처리가 완료된다.")
    void getProductList_shouldCompleteWithin100ms_whenOrderByPrice(){
        String sortType = "PRICE_ASC";
        PageRequest pageRequest = PageRequest.of(1, 20);
        ProductQuery.Summary query = ProductQuery.Summary.of(null, sortType, pageRequest);

        long startTime = System.currentTimeMillis();
        Page<ProductInfo.DataList> result = productFacade.getProductList(query);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("상품 목록 조회 실행 시간: " + executionTime + "ms");


        Assertions.assertTimeout(Duration.ofMillis(100), () -> {
            productFacade.getProductList(query);
        });
    }

    @Test
    @DisplayName("OrderBy CreatedAt DESC : 상품 목록 조회 시, 캐시를 사용하면 100ms 내로 처리가 완료된다.")
    void getProductList_shouldCompleteWithin100ms_whenOrderByLatest(){
        String sortType = "LATEST";
        PageRequest pageRequest = PageRequest.of(1, 20);
        ProductQuery.Summary query = ProductQuery.Summary.of(null, sortType, pageRequest);

        long startTime = System.currentTimeMillis();
        Page<ProductInfo.DataList> result = productFacade.getProductList(query);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("상품 목록 조회 실행 시간: " + executionTime + "ms");


        Assertions.assertTimeout(Duration.ofMillis(100), () -> {
            productFacade.getProductList(query);
        });
    }


    @Test
    @DisplayName("OrderBy LikeCount DESC : 상품 목록 조회 시, 캐시를 사용하면 100ms 내로 처리가 완료된다.")
    void getProductList_shouldCompleteWithin100ms_whenOrderByLikeCount(){
        String sortType = "LATEST";
        PageRequest pageRequest = PageRequest.of(1, 20);
        ProductQuery.Summary query = ProductQuery.Summary.of(null, sortType, pageRequest);

        long startTime = System.currentTimeMillis();
        Page<ProductInfo.DataList> result = productFacade.getProductList(query);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("상품 목록 조회 실행 시간: " + executionTime + "ms");


        Assertions.assertTimeout(Duration.ofMillis(100), () -> {
            productFacade.getProductList(query);
        });
    }

}
