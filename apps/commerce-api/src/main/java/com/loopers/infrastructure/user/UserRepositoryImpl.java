package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRespository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRespository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<UserEntity> find(String findId) {
        return userJpaRepository.findByUserId(findId);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }
}
