package com.loopers.concurrency.payment;

import com.loopers.application.payment.PaymentFacade;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.point.Point;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserCommand;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PaymentConcurrencyTest {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private PointJpaRepository pointRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private OrderRepository orderRepository;
    @MockitoBean
    private OrderService orderService;

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
        Order order = orderRepository.save(Order.create(user.getId()));

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger failCount = new AtomicInteger(0);

        when(orderService.validateOrder(any(Long.class), any(Long.class), any(BigDecimal.class)))
                .thenReturn(null);
        when(orderService.find(any(Long.class)))
                .thenReturn(order);

        String generatedKey = paymentFacade.generateKey(USER_ID, order.getId());
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    paymentFacade.pay(PaymentCommand.of(
                            USER_ID,
                            order.getId(),
                            generatedKey,
                            DEDUCT_POINT,
                            Payment.Method.POINT.name(),
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
                .isEqualTo(4);
//        verify(stockService, times(9)).increaseStock(any(Map.class));
    }
}
