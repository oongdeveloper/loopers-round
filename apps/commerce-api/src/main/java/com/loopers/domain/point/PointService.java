package com.loopers.domain.point;


import com.loopers.domain.user.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Optional<UserEntity> find(String userId){
        return pointRepository.find(userId);
    }

}
