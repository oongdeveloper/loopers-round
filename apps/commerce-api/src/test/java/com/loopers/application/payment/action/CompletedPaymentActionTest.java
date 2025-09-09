package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletedPaymentActionTest {
    @Mock
    private PaymentService paymentService;
    @Mock
    private DomainEventPublisher domainEventPublisher;

    @Test
    @DisplayName("결제가 성공하면 결제성공 Event 가 발행된다.")
    void shouldPublishEvent_whenPaymentFailed(){
        ArgumentCaptor<Collection<DomainEvent>> captor = ArgumentCaptor.forClass(Collection.class);
        CompletedPaymentAction action = new CompletedPaymentAction(paymentService, domainEventPublisher);

        Payment payment = Payment.of(PaymentCommand.of(
                1L, 1L, "test-payment-key", BigDecimal.valueOf(10000L), "CARD", null
        ));
        PaymentResult result = new PaymentResult(
                1L,
                "test-payment-key",
                BigDecimal.valueOf(10000L),
                Payment.Method.CARD,
                null,
                null,
                1,
                null
        );
        when(paymentService.findByKey(eq("test-payment-key")))
                .thenReturn(Optional.of(payment));

        action.execute(result);

        verify(domainEventPublisher, times(1)).publish(captor.capture());
        Collection<DomainEvent> capturedEvents = captor.getValue();
        assertTrue(
                capturedEvents.stream().anyMatch(e -> e instanceof PaymentEvent.Completed)
        );
    }


}
