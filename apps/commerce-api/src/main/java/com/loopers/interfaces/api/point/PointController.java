package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController implements PointV1ApiSpec{

    @GetMapping("/api/v1/points")
    @Override
    public ApiResponse<UserV1Dto.UserPointResponse> getPoint(@RequestHeader("X-USER-ID") String userId) {
        return ApiResponse.success(new UserV1Dto.UserPointResponse(
                userId,
                10000L
        ));
    }
}
