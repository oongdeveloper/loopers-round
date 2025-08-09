package com.loopers.concurrency.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LikeConcurrencyTest {
    private final LikeFacade likeFacade;
    private final LikeRepository likeRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public LikeConcurrencyTest(LikeFacade likeFacade, LikeRepository likeRepository, DatabaseCleanUp databaseCleanUp) {
        this.likeFacade = likeFacade;
        this.likeRepository = likeRepository;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("like Method 는")
    @Nested
    class ProductLikeMethod{
        @DisplayName("좋아요를 동시에 여러 번 하더라도 count 는 1번만 증가한다.")
        @Test
        void concurrencyTest_stockShouldBeProperlyWhenLike() throws InterruptedException {
            Long userId = 1L;
            Long productId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productId);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        likeFacade.like(userId, productId);
                    } catch (Exception e) {
                        System.out.println("실패: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            int likeCount = likeRepository.countBy(userId, productId);
            assertThat(likeCount).isEqualTo(1);
        }

        @DisplayName("좋아요가 Soft Deleted 상태인 경우에 다시 좋아요를 동시에 여러 번 하더라도 count 는 1번만 증가한다.")
        @Test
        void concurrencyTest_stockShouldBeProperlyWhenLikeInSoftDeleted() throws InterruptedException {
            Long userId = 1L;
            Long productId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productId);
            likeFacade.like(userId, productId);
            likeFacade.unlike(userId, productId);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        likeFacade.like(userId, productId);
                    } catch (Exception e) {
                        System.out.println("실패: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            int likeCount = likeRepository.countBy(userId, productId);
            assertThat(likeCount).isEqualTo(1);
        }
    }

}
