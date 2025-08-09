package com.loopers.domain.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@AllArgsConstructor
public class DeductPointCommand {
    private Long  userId;
    private BigDecimal deductPoint;

    public static DeductPointCommand of(Long userId, BigDecimal deductPoint) {
        return new DeductPointCommand(userId, deductPoint);
    }
}
