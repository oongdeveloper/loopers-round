package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentAppEvent;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.common.DomainEventPublisher;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.context.ApplicationEventPublisher;

public class FailedPaymentAction implements PaymentAction{
    private final PaymentService paymentService;
//    private final OrderService orderService;
//    private final StockService stockService;
//    private final UserCouponService userCouponService;

    private final DomainEventPublisher domainEventPublisher;
    private final ApplicationEventPublisher applicationEventPublisher;

    public FailedPaymentAction(PaymentService paymentService, DomainEventPublisher domainEventPublisher, ApplicationEventPublisher applicationEventPublisher) {
        this.paymentService = paymentService;
        this.domainEventPublisher = domainEventPublisher;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(PaymentResult result) {
        Payment payment = paymentService.findByKey(result.idempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + result.idempotencyKey()));

        payment.setReason(result.reason());
        payment.setPgProvider(result.pgProvider());
        payment.setPgTransactionId(result.pgTransactionKey());
        payment.failed();

        domainEventPublisher.publish(payment.pullDomainEvents());
        applicationEventPublisher.publishEvent(PaymentAppEvent.Failed.from(payment));
//        Order order = orderService.find(result.orderId());
//        order.updateStatus(Order.Status.FAILED);

//        // 쿠폰 복구
//        userCouponService.rollbackCoupon(payment.getUserId(), order.getCouponId());
//        // 재고 복구. 별도 트랜잭션
//        stockService.restoreStock(order.getLines().getOrderLineQuantity());
    }
}
