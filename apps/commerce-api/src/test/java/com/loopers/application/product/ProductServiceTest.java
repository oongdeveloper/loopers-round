package com.loopers.application.product;


import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.projections.ProductListProjectionV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private RedisCacheWrapper redisCacheWrapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // 테스트에 필요한 상수와 객체
    private Long brandId;
    private String sort;
    private Pageable pageable;
    private long mockCount;
    private Page<ProductListProjectionV2> mockPage;
    final String PRODUCT_COUNT_REDIS_PREFIX = "product:count";

    @BeforeEach
    void setUp() {
        brandId = 1L;
        sort = "price";
        pageable = PageRequest.of(0, 10);
        mockCount = 100L;
        mockPage = new PageImpl<>(Collections.emptyList(), pageable, mockCount);
    }

    @Test
    @DisplayName("Redis 캐시가 없는 경우, DB에서 count를 가져와야 한다.")
    void findByBrandIdBySortType_withoutCache() {
        when(redisCacheWrapper.get(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(Long.class)))
                .thenReturn(null);
        when(productRepository.count()).thenReturn(mockCount);
        when(productRepository.findByBrandIdBySortType(eq(brandId), eq(sort), eq(pageable)))
                .thenReturn(mockPage.getContent());

        productService.findByBrandIdBySortType(brandId, sort, pageable);

        // Redis에서 count를 가져오는 메서드가 1번 호출되었는지 확인
        verify(redisCacheWrapper, times(1)).get(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(Long.class));
        // DB에서 count를 가져오는 메서드가 1번 호출되었는지 확인
        verify(productRepository, times(1)).count();
        // Redis에 count를 저장하는 메서드가 1번 호출되었는지 확인
        verify(redisCacheWrapper, times(1)).set(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(mockCount), any(Long.class), any(TimeUnit.class));
        // DB에서 실제 데이터를 가져오는 메서드가 1번 호출되었는지 확인
        verify(productRepository, times(1)).findByBrandIdBySortType(eq(brandId), eq(sort), eq(pageable));
    }

    @Test
    @DisplayName("Redis 캐시가 있는 경우, DB에서 count를 가져오지 않아야 한다.")
    void findByBrandIdBySortType_withCache() {
        when(redisCacheWrapper.get(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(Long.class)))
                .thenReturn(mockCount);
        when(productRepository.findByBrandIdBySortType(eq(brandId), eq(sort), eq(pageable)))
                .thenReturn(mockPage.getContent());

        productService.findByBrandIdBySortType(brandId, sort, pageable);

        // Redis에서 count를 가져오는 메서드가 1번 호출되었는지 확인
        verify(redisCacheWrapper, times(1)).get(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(Long.class));
        // DB에서 count를 가져오는 메서드는 호출되지 않았는지 확인
        verify(productRepository, never()).count();
        // Redis에 count를 저장하는 메서드는 호출되지 않았는지 확인
        verify(redisCacheWrapper, never()).set(eq(PRODUCT_COUNT_REDIS_PREFIX), eq(mockCount), any(Long.class), any(TimeUnit.class));
        // DB에서 실제 데이터를 가져오는 메서드가 1번 호출되었는지 확인
        verify(productRepository, times(1)).findByBrandIdBySortType(eq(brandId), eq(sort), eq(pageable));
    }
}
