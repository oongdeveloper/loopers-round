package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "Point 기능 API 입니다.")
public interface PointV1ApiSpec {
    @Operation(
            summary = "Point 조회",
            description = "ID로 보유 Point를 조회합니다."
    )
    ApiResponse<UserV1Dto.UserPointResponse> get(
            @Schema(name = "USER ID", description = "조회할 User의 ID")
            String userId
    );

    @Operation(
            summary = "Point 충전",
            description = "User 의 포인트를 충전합니다."
    )
    ApiResponse<UserV1Dto.UserPointResponse> charge(
            @Schema(name = "USER ID", description = "충전할 User의 ID")
            String userId,
            @Schema(name = "Charge Point", description = "충전할 포인트")
            UserV1Dto.ChargePointRequest chargePoint
    );
}
