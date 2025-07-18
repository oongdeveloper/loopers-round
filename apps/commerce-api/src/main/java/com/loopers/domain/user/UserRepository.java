package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> find(String findId);
    UserEntity save(UserEntity user);
}
