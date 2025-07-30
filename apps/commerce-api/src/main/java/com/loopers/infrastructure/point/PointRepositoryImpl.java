package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl(PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public Optional<UserEntity> find(String findId) {
        return pointJpaRepository.findByUserId(findId);
    }
}
