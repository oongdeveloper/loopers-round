package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class PointController implements PointV1ApiSpec{
    private final PointFacade pointFacade;

    public PointController(PointFacade pointFacade) {
        this.pointFacade = pointFacade;
    }

    @GetMapping("/api/v1/points")
    @Override
    public ApiResponse<PointV1Dto.UserPointResponse> get(@RequestHeader("X-USER-ID") String userId) {
        PointInfo pointInfo = pointFacade.get(userId);
        return ApiResponse.success(new PointV1Dto.UserPointResponse(
                pointInfo.userId(),
                pointInfo.point()
        ));
    }

    @PostMapping("/api/v1/points/charge")
    @Override
    public ApiResponse<PointV1Dto.UserPointResponse> charge(@RequestHeader("X-USER-ID") String userId, @RequestBody PointV1Dto.ChargePointRequest request) {
        BigDecimal chargedPoint = pointFacade.charge(userId, request);
        return ApiResponse.success(new PointV1Dto.UserPointResponse(
                userId,
                chargedPoint
        ));
    }
}
