package com.loopers.application.coupon;

import com.loopers.domain.coupons.issued.UserCouponCommand;
import com.loopers.domain.order.OrderEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CouponEventListener {
    private final CouponFacade couponFacade;

    public CouponEventListener(CouponFacade couponFacade) {
        this.couponFacade = couponFacade;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderEvent.Created event){
        couponFacade.use(UserCouponCommand.Use.of(event.userId(), event.couponId(), event.finalPrice()));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handle(OrderEvent.Canceled event){
        couponFacade.rollback(UserCouponCommand.Rollback.of(event.userId(), event.couponId()));
    }
}
