package com.loopers.domain.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@AllArgsConstructor
public class DeductPointCommand {
    private String  userId;
    private BigDecimal deductPoint;

    public static DeductPointCommand of(String userId, BigDecimal deductPoint) {
        return new DeductPointCommand(userId, deductPoint);
    }
}
