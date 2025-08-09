package com.loopers.concurrency.stock;

import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockService;
import com.loopers.infrastructure.stock.StockJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class StockConcurrencyTest {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockJpaRepository stockJpaRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("동시에 여러 번 요청이 오더라도 재고는 정상적으로 차감되어야 한다.")
    void shouldDeductStockCorrectly_whenMultipleRequestsAreMadeConcurrently() throws InterruptedException {
        stockJpaRepository.save(Stock.from(1L, 10L));
        stockJpaRepository.save(Stock.from(2L, 10L));
        stockJpaRepository.save(Stock.from(3L, 10L));

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Map request = Map.of(1L, 2L);
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    stockService.decreaseStock(request);
                } catch (RuntimeException e){
                    // TODO. Exception 을 먹어버림.
                    System.out.println("실패 " + e);
                } finally{
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockService.find(1L);
        assertThat(stock.getQuantity()).isEqualTo(0);
    }
}
