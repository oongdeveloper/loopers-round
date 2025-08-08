package com.loopers.domain.point;

import java.math.BigDecimal;

public class PointCommand {

    public record Charge(
            Long userId,
            BigDecimal amount
    ){
        public static Charge of(Long userId, BigDecimal chargePoint) {
            return new Charge(userId, chargePoint);
        }
    }

    public record Deduct(
            Long userId,
            BigDecimal amount
    ) {
        public static Deduct of(Long userId, BigDecimal deductPoint) {
            return new Deduct(userId, deductPoint);
        }
    }
}
