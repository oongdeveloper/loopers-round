package com.loopers.concurrency.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointConcurrencyTest {
    @Autowired
    private PointService pointService;
    @Autowired
    private UserService userService;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("동시에 요청이 여러 번 오더라도, 포인트는 정확하게 차감되어야 한다.")
    void shouldDeductPointsCorrectly_whenMultipleRequestsAreMadeConcurrently() throws InterruptedException {
        User user = userService.save(UserCommand.of(
                "oong",
                "오옹",
                User.Gender.M,
                "2025-06-01",
                "oong@oo.ng"
        ));
        Long USER_ID = user.getId();
        BigDecimal TOTAL_POINT = BigDecimal.valueOf(10000L);
        BigDecimal DEDUCT_POINT = BigDecimal.valueOf(800L);

        pointService.save(Point.from(USER_ID, TOTAL_POINT));

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    pointService.deduct(PointCommand.Deduct.of(USER_ID, DEDUCT_POINT));
                } catch (RuntimeException e){
                    // TODO. Exception 을 먹어버림.
                    System.out.println("실패 " + e);
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();

        Point point = pointService.find(USER_ID);
        assertThat(point.getPoint().compareTo(TOTAL_POINT.subtract(DEDUCT_POINT.multiply(BigDecimal.valueOf(threadCount)))))
                .isEqualTo(0);
    }


}
