package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentAppEvent;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.common.DomainEventPublisher;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.context.ApplicationEventPublisher;

public class CompletedPaymentAction implements PaymentAction{
    private final PaymentService paymentService;
//    private final OrderService orderService;
    private final DomainEventPublisher domainEventPublisher;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CompletedPaymentAction(PaymentService paymentService, DomainEventPublisher domainEventPublisher, ApplicationEventPublisher applicationEventPublisher) {
        this.paymentService = paymentService;
        this.domainEventPublisher = domainEventPublisher;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(PaymentResult result) {
        Payment payment = paymentService.findByKey(result.idempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + result.idempotencyKey()));

//        payment.updateStatus(COMPLETED);
        payment.setReason(result.reason());
        payment.setPgProvider(result.pgProvider() == null ? 0 : result.pgProvider());
        payment.setPgTransactionId(result.pgTransactionKey());
        payment.completed();
        // EventPublish 어떻게 할거냐
//        paymentService.save(payment);

        domainEventPublisher.publish(payment.pullDomainEvents());
        applicationEventPublisher.publishEvent(PaymentAppEvent.Completed.from(payment));
//        orderService.find(payment.getOrderId())
//                    .updateStatus(Order.Status.COMPLETED);
    }
}
