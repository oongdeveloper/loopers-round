package com.loopers.application.point;

import com.loopers.domain.point.ChargePointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserEntity;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class PointFacade {
    private final PointService pointService;

    public PointFacade(PointService pointService) {
        this.pointService = pointService;
    }

    @Transactional
    public long chargePoint(String userId, UserV1Dto.ChargePointRequest chargePointRequest) {
        ChargePointCommand chargeCommand = ChargePointCommand.of(userId, chargePointRequest.point());

        // UserId 가 있는지 없는지 검사
        UserEntity user = pointService.find(chargeCommand.getUserId()).orElseThrow(
                () -> {
                    throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다.");
                });
        return pointService.charge(user, chargePointRequest.point());
    }
}
