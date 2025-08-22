package com.loopers.application.payment.action;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.stock.StockService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import static com.loopers.domain.payment.Payment.*;

public class FailedPaymentAction implements PaymentAction{
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final StockService stockService;
    private final UserCouponService userCouponService;

    public FailedPaymentAction(PaymentService paymentService, OrderService orderService, StockService stockService, UserCouponService userCouponService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.stockService = stockService;
        this.userCouponService = userCouponService;
    }

    @Override
    public void execute(PaymentResult result) {
        Payment payment = paymentService.findByKey(result.idempotencyKey())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "잘못된 결제 요청입니다. " + result.idempotencyKey()));
        payment.updateStatus(Status.FAILED);
        payment.setReason(result.reason());
        payment.setPgProvider(result.pgProvider());
        payment.setPgTransactionId(result.pgTransactionKey());

        Order order = orderService.find(result.orderId());
        order.updateStatus(Order.Status.FAILED);

        // 쿠폰 복구
        userCouponService.rollbackCoupon(payment.getUserId(), order.getCouponId());
        // 재고 복구. 별도 트랜잭션
        stockService.restoreStock(order.getLines().getOrderLineQuantity());
    }
}
