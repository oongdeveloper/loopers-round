package com.loopers.application.point;

import com.loopers.domain.point.ChargePointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    public PointFacade(PointService pointService, UserService userService) {
        this.pointService = pointService;
        this.userService = userService;
    }

    public BigDecimal charge(String userId, PointV1Dto.ChargePointRequest chargePointRequest) {
        userService.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
        ChargePointCommand chargeCommand = ChargePointCommand.of(userId, chargePointRequest.point());
        return pointService.charge(chargeCommand);
    }

    public PointInfo get(String userId){
        return pointService.find(userId)
                .map(PointInfo::from)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
    }
}
