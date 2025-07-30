package com.loopers.domain.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ChargePointCommand {

    private String userId;
    private long chargePoint;

    public static ChargePointCommand of(String userId, long chargePoint) {
        return new ChargePointCommand(userId, chargePoint);
    }
}
