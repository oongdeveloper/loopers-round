package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User V1 API", description = "사용자 관련 API 입니다.")
public interface UserV1ApiSpec {

    ApiResponse<UserV1Dto.UserResponse> signUp(
            UserV1Dto.SignUpRequest request
    );
}
