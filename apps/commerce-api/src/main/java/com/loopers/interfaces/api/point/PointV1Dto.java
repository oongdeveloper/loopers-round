package com.loopers.interfaces.api.point;

public class PointV1Dto {

    public record ChargePointRequest(
            long point
    ) {
    }

    public record UserPointResponse(
            String userId,
            long point
    ){}
}
