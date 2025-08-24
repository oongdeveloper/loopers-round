package com.loopers.application.payment.processor;

import com.loopers.application.payment.action.CompletedPaymentAction;
import com.loopers.application.payment.action.FailedPaymentAction;
import com.loopers.application.payment.action.PaymentAction;
import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.stock.StockService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PaymentPostProcessor {
    private final Map<PaymentResult.Status, PaymentAction> actions = new HashMap<>();

    public PaymentPostProcessor(PaymentService paymentService, OrderService orderService, StockService stockService, UserCouponService userCouponService) {
        actions.put(PaymentResult.Status.COMPLETED, new CompletedPaymentAction(paymentService, orderService));
        actions.put(PaymentResult.Status.FAILED, new FailedPaymentAction(paymentService, orderService, stockService, userCouponService));
    }

    @Transactional
    public void postprocess(PaymentResult result) {
        if (result.status() == null) return;

        PaymentAction action = actions.get(result.status());
        if (action != null) {
            action.execute(result);
        } else {
            throw new UnsupportedOperationException("Unsupported payment status: " + result.status());
        }
    }
}
