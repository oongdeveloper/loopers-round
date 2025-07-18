package com.loopers.application.point;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.point.ChargePointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserEntity;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PointFacade {
    private final PointService pointService;

    public PointFacade(PointService pointService) {
        this.pointService = pointService;
    }

    public long chargePoint(String userId, PointV1Dto.ChargePointRequest chargePointRequest) {
        ChargePointCommand chargeCommand = ChargePointCommand.of(userId, chargePointRequest.point());
        return pointService.charge(chargeCommand);
    }

    public PointInfo find(String userId){
        return pointService.find(userId)
                .map(PointInfo::from)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
    }
}
