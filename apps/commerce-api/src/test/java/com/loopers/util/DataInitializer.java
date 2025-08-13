package com.loopers.util;

import com.loopers.domain.brand.Brand;
import com.loopers.fixture.BrandFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
//@JdbcTest
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
public class DataInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void brandDataInitializer() {
        int batchSize = 1000;

        // 여기서 브랜드 데이터 1000개 만들고
        List<Brand> brands = IntStream.range(0, 1000)
                .mapToObj(i -> BrandFixture.persistence()
                        .brandName("brand-test-"+i)
                        .build())
                .collect(Collectors.toList());
        // 쿼리 만들고
        String sql = "INSERT INTO brand (brand_name, logo_url, created_at, updated_at) VALUES (?, ?, ?, ?)";

        ZonedDateTime now = ZonedDateTime.now();

        // 여기서 bulk insert 1000건이 한 번에 들어감
        jdbcTemplate.batchUpdate(sql, brands, batchSize, (ps, brand) -> {
            ps.setString(1, brand.getBrandName());
            ps.setString(2, brand.getLogoUrl());
            ps.setTimestamp(3, Timestamp.from(now.toInstant()));
            ps.setTimestamp(4, Timestamp.from(now.toInstant()));
        });
    }

    @Test
    void productDataInitializer() {
        final int EXECUTE_COUNT = 500;
        final int BULK_INSERT_SIZE = 2000;
        final int threadCount = 10;

        try{
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    String sql = "INSERT INTO product (ref_brand_id, product_name, price, published_at, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW(), NOW())";
                    ThreadLocalRandom random = ThreadLocalRandom.current();

                    for (int batch = 0; batch < EXECUTE_COUNT; batch++) {
                        List<Object[]> batchArgs = new ArrayList<>();

                        for (int j = 0; j < BULK_INSERT_SIZE; j++) {
                            // 위에 브랜드 1000개 들어가서 1~1000 중 아무 숫자
                            long randomBrandId = random.nextLong(1, 1001);

                            batchArgs.add(new Object[]{
                                    randomBrandId,
                                    "Product-" + randomBrandId + "-" + finalI + "-" + j,
                                    random.nextLong(1000, 100000), // current() 호출 제거
                            });
                        }

                        jdbcTemplate.batchUpdate(sql, batchArgs);
                        log.info("Thread {} - Batch {} completed", Thread.currentThread().getName(), batch);
                    }
                });
            }
//            executorService.shutdown();
            // 작업 완료를 대기
            boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
            if (!terminated) {
                log.warn("ExecutorService did not terminate in the specified time. Forcing shutdown.");
                executorService.shutdownNow(); // 강제 종료
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void likeDataInitializer() {
        final int EXECUTE_COUNT = 200 ;
        final int BULK_INSERT_SIZE = 2000;
        final int threadCount = 10;

        try{
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    String sql = "INSERT INTO likes (ref_user_id, ref_product_id, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
                    ThreadLocalRandom random = ThreadLocalRandom.current();

                    for (int batch = 0; batch < EXECUTE_COUNT; batch++) {
                        List<Object[]> batchArgs = new ArrayList<>();

                        for (int j = 0; j < BULK_INSERT_SIZE; j++) {
                            // 위에 브랜드 1000개 들어가서 1~1000 중 아무 숫자
                            long userId = random.nextLong(1, 100001);
                            long randomProductId = random.nextLong(1, 10000001);

                            batchArgs.add(new Object[]{
                                    userId,
                                    randomProductId
                            });
                        }

                        jdbcTemplate.batchUpdate(sql, batchArgs);
                        log.info("Thread {} - Batch {} completed", Thread.currentThread().getName(), batch);
                    }
                });
            }
//            executorService.shutdown();
            // 작업 완료를 대기
            boolean terminated = executorService.awaitTermination(5, TimeUnit.MINUTES);
            if (!terminated) {
                log.warn("ExecutorService did not terminate in the specified time. Forcing shutdown.");
                executorService.shutdownNow(); // 강제 종료
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
