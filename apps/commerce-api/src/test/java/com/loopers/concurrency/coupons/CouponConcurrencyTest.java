package com.loopers.concurrency.coupons;

import com.loopers.application.order.OrderCommand;
import com.loopers.domain.coupons.issued.UserCoupon;
import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.coupons.master.Coupon;
import com.loopers.infrastructure.coupons.issued.UserCouponJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootTest
public class CouponConcurrencyTest {

    private final UserCouponService userCouponService;
    private final UserCouponJpaRepository jpaRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public CouponConcurrencyTest(UserCouponService userCouponService, UserCouponJpaRepository jpaRepository, DatabaseCleanUp databaseCleanUp) {
        this.userCouponService = userCouponService;
        this.jpaRepository = jpaRepository;
        this.databaseCleanUp = databaseCleanUp;
    }


    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("Coupon Method 는")
    @Nested
    class ProductLikeMethod {
        @DisplayName("Coupon 을 동시에 여러 번 사용하더라도 1명만 사용에 성공해야 한다.")
        @Test
        void concurrencyTest_stockShouldBeProperlyWhenLike() throws InterruptedException {
            Long userId = 1L;
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAtIn7Days = now.plusDays(7);

            // 1. 정액 할인 쿠폰 생성
            UserCoupon fixedAmountCoupon = UserCoupon.of(
                    userId,
                    1L,
                    expiresAtIn7Days,
                    "10,000원 할인 쿠폰",
                    Coupon.CouponType.FIXED,
                    "ALL",
                    null,
                    new BigDecimal("50000"),
                    new BigDecimal("10000"),
                    new BigDecimal("30000")
            );
            UserCoupon rateCoupon = UserCoupon.of(
                    userId,
                    2L,
                    expiresAtIn7Days,
                    "20% 할인 쿠폰",
                    Coupon.CouponType.RATE,
                    "ALL",
                    null,
                    new BigDecimal("20000"),
                    new BigDecimal("20"),
                    new BigDecimal("50000")
            );

            jpaRepository.save(fixedAmountCoupon);
            jpaRepository.save(rateCoupon);

            List<OrderCommand.ItemCreate> items = IntStream.range(0, 5)
                    .mapToObj(i -> new OrderCommand.ItemCreate(
                            Long.valueOf(i), Long.valueOf(i)
                    ))
                    .toList();

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        userCouponService.applyCoupon(
                            UserCouponCommand.Apply.of(
                                    userId,
                                    1L,
                                    BigDecimal.valueOf(10000L)
                            )
                        );

                    } catch (Exception e) {
                        System.out.println("실패: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }

    }
}
