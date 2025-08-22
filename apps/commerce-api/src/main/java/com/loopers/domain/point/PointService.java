package com.loopers.domain.point;


import com.loopers.domain.point.exception.InsufficientPointsException;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point find(Long userId){
        return pointRepository.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
    }

    public Point save(Point point) {
        return pointRepository.save(point);
    }

    @Transactional
    public BigDecimal charge(ChargePointCommand command) {
        Point point = pointRepository.findForUpdate(command.getUserId())
                .orElseGet(() -> pointRepository.save(Point.from(Long.valueOf(command.getUserId()), BigDecimal.ZERO)));
        return point.charge(command.getChargePoint());
    }

    @Transactional
    public BigDecimal deduct(PointCommand.Deduct command) throws InsufficientPointsException {
        Point point = pointRepository.findForUpdate(command.userId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
        return point.deduct(command.amount());
    }

}
