package com.loopers.domain.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@AllArgsConstructor
public class ChargePointCommand {

    private Long userId;
    private BigDecimal chargePoint;

    public static ChargePointCommand of(Long userId, BigDecimal chargePoint) {
        return new ChargePointCommand(userId, chargePoint);
    }
}
