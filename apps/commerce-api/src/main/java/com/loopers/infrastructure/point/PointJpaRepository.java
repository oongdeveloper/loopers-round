package com.loopers.infrastructure.point;

import com.loopers.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String findId);
}
