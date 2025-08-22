package com.loopers.application.payment;

import com.loopers.domain.coupons.issued.UserCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.stock.StockService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import static com.loopers.domain.payment.Payment.Status.*;

@Component
public class PostPaymentHandler {
    private final OrderService orderService;
    private final StockService stockService;
    private final UserCouponService userCouponService;

    public PostPaymentHandler(OrderService orderService, StockService stockService, UserCouponService userCouponService) {
        this.orderService = orderService;
        this.stockService = stockService;
        this.userCouponService = userCouponService;
    }

    @Transactional
    public void postPayment(Payment payment) {
        if(payment.getStatus().equals(COMPLETED)){
            orderService.find(payment.getOrderId())
                    .updateStatus(Order.Status.COMPLETED);
        }

        // 실패 시, 롤백
        // 단 시스템 오류의 경우 처리하지 않는다.
        else if (payment.getStatus().equals(FAILED)){
            Order order = orderService.find(payment.getOrderId());
            order.updateStatus(Order.Status.FAILED);
            // 쿠폰 복구
            userCouponService.rollbackCoupon(payment.getUserId(), order.getCouponId());
            // 재고 복구. 별도 트랜잭션
            stockService.restoreStock(order.getLines().getOrderLineQuantity());
        }
    }
}
