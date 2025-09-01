package com.loopers.application.coupon;


import com.loopers.support.event.AppErrorEvent;

public record CouponRollbackFailedEvent(
        Long userId,
        Long couponId,
        String reason
) implements AppErrorEvent {
    public static CouponRollbackFailedEvent of(Long userId, Long couponId, String reason){
        return new CouponRollbackFailedEvent(userId, couponId, reason);
    }
}
