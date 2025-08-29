package com.loopers.application.like;

import com.loopers.domain.common.DomainEvent;
import com.loopers.domain.common.DomainEventPublisher;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LikeFacadeTest {
    @MockitoBean
    private LikeService mockLikeService;
    @MockitoBean
    private DomainEventPublisher mockDomainEventPublisher;

    @Autowired
    private LikeFacade likeFacade;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("좋아요를 하면 Liked Event 가 발행된다.")
    void shouldPublishEvent_whenLiked() {
        ArgumentCaptor<Collection<DomainEvent>> captor = ArgumentCaptor.forClass(Collection.class);
        Payment payment = Payment.of(PaymentCommand.of(
                1L, 1L, "test-payment-key", BigDecimal.valueOf(10000L), "CARD", null
        ));
        Like mockLike = Like.of(1L, 1L);

        when(mockLikeService.find(eq(mockLike.getId().getUserId()), eq(mockLike.getId().getProductId())))
                .thenReturn(Optional.of(mockLike));

        likeFacade.like(1L, 1L);

        verify(mockDomainEventPublisher, times(1)).publish(captor.capture());
        Collection<DomainEvent> capturedEvents = captor.getValue();
        assertTrue(
                capturedEvents.stream().anyMatch(e -> e instanceof LikeEvent.Liked)
        );
    }

    @Test
    @DisplayName("좋아요를 하면  Unliked Event 가 발행된다.")
    void shouldPublishEvent_whenUnLiked() {
        ArgumentCaptor<Collection<DomainEvent>> captor = ArgumentCaptor.forClass(Collection.class);
        Payment payment = Payment.of(PaymentCommand.of(
                1L, 1L, "test-payment-key", BigDecimal.valueOf(10000L), "CARD", null
        ));
        Like mockLike = Like.of(1L, 1L);

        when(mockLikeService.find(eq(mockLike.getId().getUserId()), eq(mockLike.getId().getProductId())))
                .thenReturn(Optional.of(mockLike));

        likeFacade.unlike(1L, 1L);

        verify(mockDomainEventPublisher, times(1)).publish(captor.capture());
        Collection<DomainEvent> capturedEvents = captor.getValue();
        assertTrue(
                capturedEvents.stream().anyMatch(e -> e instanceof LikeEvent.UnLiked)
        );
    }
}
