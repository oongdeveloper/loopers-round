package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import static com.loopers.domain.payment.Payment.Status.COMPLETED;

public class CompletedPaymentAction implements PaymentAction{
    private final PaymentService paymentService;
    private final OrderService orderService;

    public CompletedPaymentAction(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @Override
    public void execute(PaymentResult result) {
        Payment payment = paymentService.findByKey(result.idempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + result.idempotencyKey()));

        payment.updateStatus(COMPLETED);
        payment.setReason(result.reason());
        payment.setPgProvider(result.pgProvider());
        payment.setPgTransactionId(result.pgTransactionKey());

        orderService.find(payment.getOrderId())
                    .updateStatus(Order.Status.COMPLETED);
    }
}
