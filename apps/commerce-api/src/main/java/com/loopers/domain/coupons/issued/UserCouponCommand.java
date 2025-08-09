package com.loopers.domain.coupons.issued;

import java.math.BigDecimal;

public class UserCouponCommand {

    public record Apply(
            Long userId,
            Long couponId,
            BigDecimal amount
    ){
        public static Apply of(Long userId, Long couponId, BigDecimal amount) {
            return new Apply(userId, couponId, amount);
        }
    }
}
