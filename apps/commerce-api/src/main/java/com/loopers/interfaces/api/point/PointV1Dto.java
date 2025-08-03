package com.loopers.interfaces.api.point;

import java.math.BigDecimal;

public class PointV1Dto {

    public record ChargePointRequest(
            BigDecimal point
    ) {
    }

    public record UserPointResponse(
            String userId,
            BigDecimal point
    ){}
}
