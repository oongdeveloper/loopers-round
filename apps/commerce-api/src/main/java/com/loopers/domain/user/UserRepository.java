package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    public Optional<UserEntity> find(String findId);

    public UserEntity save(UserEntity user);
}
