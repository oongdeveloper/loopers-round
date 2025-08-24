package com.loopers.concurrency.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.domain.point.Point;
import com.loopers.domain.stock.StockService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentConcurrencyTest {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private PointJpaRepository pointRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @MockitoSpyBean
    private StockService stockService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("동시에 요청이 여러 번 오더라도, 결제는 한번만 되어야 한다.")
    void shouldDeductPointsCorrectly_whenMultipleRequestsAreMadeConcurrently() throws InterruptedException {
        User user = userJpaRepository.save(User.from(UserCommand.of(
                "oong",
                "오옹",
                User.Gender.M,
                "2025-06-01",
                "oong@oo.ng"
        )));
        Long USER_ID = user.getId();
        BigDecimal TOTAL_POINT = BigDecimal.valueOf(10000L);
        BigDecimal DEDUCT_POINT = BigDecimal.valueOf(1000L);

        pointRepository.save(Point.from(USER_ID, TOTAL_POINT));

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    paymentFacade.pay(PaymentCommand.of(
                            USER_ID,
                            1L,
                            null,
                            BigDecimal.ZERO,
                            Payment.Method.CARD.name(),
                            null
                    ));
                } catch (RuntimeException e){
                    // TODO. Exception 을 먹어버림.
                    System.out.println("실패 " + e);
                    failCount.incrementAndGet();
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertThat(failCount.get())
                .isEqualTo(9);
//        verify(stockService, times(9)).increaseStock(any(Map.class));
    }
}
