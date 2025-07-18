package com.loopers.domain.point;


import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> find(String userId){
        return pointRepository.find(userId);
    }

    @Transactional
    public long charge(ChargePointCommand command) {
        UserEntity user = find(command.getUserId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
        return user.charge(command.getChargePoint());
    }

}
