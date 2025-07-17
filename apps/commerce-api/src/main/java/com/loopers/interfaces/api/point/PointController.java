package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import org.springframework.web.bind.annotation.*;

@RestController
public class PointController implements PointV1ApiSpec{

    private final PointService pointService;
    private final PointFacade pointFacade;

    public PointController(PointService pointService, PointFacade pointFacade) {
        this.pointService = pointService;
        this.pointFacade = pointFacade;
    }

    @GetMapping("/api/v1/points")
    @Override
    public ApiResponse<UserV1Dto.UserPointResponse> get(@RequestHeader("X-USER-ID") String userId) {
        return ApiResponse.success(new UserV1Dto.UserPointResponse(
                userId,
                10000L
        ));
    }

    @PostMapping("/api/v1/points/charge")
    @Override
    public ApiResponse<UserV1Dto.UserPointResponse> charge(@RequestHeader("X-USER-ID") String userId, @RequestBody UserV1Dto.ChargePointRequest request) {
        long chargedPoint = pointFacade.chargePoint(userId, request);
        return ApiResponse.success(new UserV1Dto.UserPointResponse(
                userId,
                chargedPoint
        ));
    }
}
