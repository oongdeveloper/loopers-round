package com.loopers.domain.point;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Point> find(String userId){
        return pointRepository.find(userId);
    }

    public Point save(Point point) {
        return pointRepository.save(point);
    }

    @Transactional
    public BigDecimal charge(ChargePointCommand command) {
        Point point = find(command.getUserId())
                .orElseGet(() -> pointRepository.save(Point.from(command.getUserId(), BigDecimal.ZERO)));
        return point.charge(command.getChargePoint());
    }

    @Transactional
    public BigDecimal deduct(DeductPointCommand command){
        Point point = find(command.getUserId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
        return point.deduct(command.getDeductPoint());
    }

}
