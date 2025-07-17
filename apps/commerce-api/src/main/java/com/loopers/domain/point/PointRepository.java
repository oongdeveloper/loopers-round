package com.loopers.domain.point;

import com.loopers.domain.user.UserEntity;

import java.util.Optional;

public interface PointRepository {
    Optional<UserEntity> find(String findId);
}
