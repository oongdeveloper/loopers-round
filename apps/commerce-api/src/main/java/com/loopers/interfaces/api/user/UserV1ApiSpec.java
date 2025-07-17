package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User V1 API", description = "사용자 관련 API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
            summary = "User 회원가입",
            description = "User 정보로 회원가입합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> signUp(
            @Schema(name = "USER 정보", description = "회원가입할 User 정보")
            UserV1Dto.SignUpRequest request
    );

    @Operation(
            summary = "User 조회",
            description = "User 정보를 조회합니다."
    )
    public ApiResponse<UserV1Dto.UserResponse> find(
            @Schema(name = "USER ID 정보", description = "User 정보")
            String userId
    );
}
