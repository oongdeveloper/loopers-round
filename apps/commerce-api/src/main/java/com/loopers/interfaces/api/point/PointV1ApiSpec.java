package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.example.ExampleV1Dto;
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
    ApiResponse<UserV1Dto.UserPointResponse> getPoint(
            @Schema(name = "USER ID", description = "조회할 User의 ID")
            String userId
    );
}
