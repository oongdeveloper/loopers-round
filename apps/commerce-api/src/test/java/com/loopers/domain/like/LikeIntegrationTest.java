package com.loopers.domain.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.env.IntegrationTest;
import com.loopers.utils.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@IntegrationTest
@SpringBootTest
public class LikeIntegrationTest {

    private final LikeFacade likeFacade;
    private final LikeRepository likeRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public LikeIntegrationTest(LikeFacade likeFacade, LikeRepository likeRepository, DatabaseCleanUp databaseCleanUp) {
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

        @Test
        @DisplayName("좋아요 추가 시 여러번 하더라도 동일하게 성공해야 한다.")
        void shouldBeIdempotent_forLikeAndUnlike(){
            Long userId = 1L;
            Long productCatalogId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productCatalogId);
            likeFacade.like(userId, productCatalogId);

            Optional<Like> initialLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(initialLike).isPresent();
            assertThat(initialLike.get().getDeletedAt()).isNull();
            assertThat(initialLike.get().getId().getUserId()).isEqualTo(userId);
            assertThat(initialLike.get().getId().getProductId()).isEqualTo(productCatalogId);

            likeFacade.like(userId, productCatalogId);

            // TODO. 이걸 어떻게 확인해야 할까?
            Optional<Like> finalLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(finalLike).isPresent();
            assertThat(finalLike.get().getDeletedAt()).isNull();
            assertThat(finalLike.get().getId().getUserId()).isEqualTo(userId);
            assertThat(finalLike.get().getId().getProductId()).isEqualTo(productCatalogId);
        }

        @DisplayName("좋아요를 동시에 여러 번 하더라도 count 는 1번만 증가한다.")
        @Test
        void concurrencyTest_stockShouldBeProperlyWhenLike() throws InterruptedException {
            Long userId = 1L;
            Long productId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productId);

            int threadCount = 10;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            // TODO. 왜 select 이 2번 나가지?
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

        @Test
        @DisplayName("좋아요가 삭제된 상태인 경우 복원되어야 한다.")
        void shouldRestoreLike_whenLikeIsSoftDeleted(){
            Long userId = 1L;
            Long productCatalogId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productCatalogId);
            likeFacade.like(userId, productCatalogId);
            likeFacade.unlike(userId, productCatalogId);

            Optional<Like> initialLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(initialLike).isPresent();
            assertThat(initialLike.get().getDeletedAt()).isNotNull();

            likeFacade.like(userId, productCatalogId);

            Optional<Like> finalLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(finalLike).isPresent();
            assertThat(finalLike.get().getDeletedAt()).isNull();
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

    @DisplayName("unlike Method 는")
    @Nested
    class ProductUnLikeMethod{
        @Test
        @DisplayName("좋아요가 활성 상태일 경우, 삭제 상태로 변경한다.")
        void shouldSoftDeleteLike_whenLikeIsActive() {
            Long userId = 1L;
            Long productCatalogId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productCatalogId);
            likeFacade.like(userId, productCatalogId);

            Optional<Like> initialLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(initialLike).isPresent();
            assertThat(initialLike.get().getDeletedAt()).isNull();

            likeFacade.unlike(userId, productCatalogId);

            Optional<Like> finalLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(finalLike).isPresent();
            assertThat(finalLike.get().getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("좋아요가 존재하지 않을 경우, 아무 동작도 하지 않는다.")
        void shouldNotChange_whenLikeDoesNotExist() {
            Long userId = 1L;
            Long productCatalogId = 1L;
            final Like.LikeId TEST_LIKE_ID =Like.LikeId.of(userId, productCatalogId);

            likeFacade.unlike(userId,productCatalogId);

            Optional<Like> foundLike = likeRepository.findById(TEST_LIKE_ID);
            assertThat(foundLike).isNotPresent();
        }
    }

}
