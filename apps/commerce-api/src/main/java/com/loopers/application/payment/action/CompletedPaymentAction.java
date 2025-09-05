package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.shared.DomainEventPublisher;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class CompletedPaymentAction implements PaymentAction{
    private final PaymentService paymentService;
    private final DomainEventPublisher domainEventPublisher;

    public CompletedPaymentAction(PaymentService paymentService, DomainEventPublisher domainEventPublisher) {
        this.paymentService = paymentService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void execute(PaymentResult result) {
        Payment payment = paymentService.findByKey(result.idempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + result.idempotencyKey()));

        payment.setReason(result.reason());
        payment.setPgProvider(result.pgProvider() == null ? 0 : result.pgProvider());
        payment.setPgTransactionId(result.pgTransactionKey());
        payment.completed();

        domainEventPublisher.publish(payment.pullDomainEvents());
    }
}
