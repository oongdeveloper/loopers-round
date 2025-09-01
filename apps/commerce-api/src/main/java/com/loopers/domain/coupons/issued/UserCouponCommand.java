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

    public record Use(
            Long userId,
            Long couponId,
            BigDecimal amount
    ){
        public static Use of(Long userId, Long couponId, BigDecimal amount) {
            return new Use(userId, couponId, amount);
        }
    }

    public record Rollback(
            Long userId,
            Long couponId
    ) {
        public static Rollback of(Long userId, Long couponId) {
            return new Rollback(userId, couponId);
        }
    }
}
