package com.loopers.application.point;

import com.loopers.domain.point.ChargePointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.point.PointV1Dto;
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

    public PointInfo charge(String userId, PointV1Dto.ChargePointRequest chargePointRequest) {
        User user = userService.find(userId);
        ChargePointCommand chargeCommand = ChargePointCommand.of(user.getId(), chargePointRequest.point());

        BigDecimal chargedPoint = pointService.charge(chargeCommand);
        return new PointInfo(user.getId(), chargedPoint);
//        return pointService.charge(chargeCommand);
    }

    public PointInfo get(String userId){
        User user = userService.find(userId);

        return PointInfo.from(pointService.find(user.getId()));
    }
}
