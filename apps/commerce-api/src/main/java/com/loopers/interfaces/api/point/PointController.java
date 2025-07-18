package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
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
    public ApiResponse<PointV1Dto.UserPointResponse> get(@RequestHeader("X-USER-ID") String userId) {
        PointInfo pointInfo = pointFacade.find(userId);
        // TODO. PointInfo 의 값이 인자로 받은 userId 와 같은지 검증을 추가해야 하나?
        return ApiResponse.success(new PointV1Dto.UserPointResponse(
                pointInfo.userId(),
                pointInfo.point()
        ));
    }

    @PostMapping("/api/v1/points/charge")
    @Override
    public ApiResponse<PointV1Dto.UserPointResponse> charge(@RequestHeader("X-USER-ID") String userId, @RequestBody PointV1Dto.ChargePointRequest request) {
        long chargedPoint = pointFacade.chargePoint(userId, request);
        return ApiResponse.success(new PointV1Dto.UserPointResponse(
                userId,
                chargedPoint
        ));
    }
}
