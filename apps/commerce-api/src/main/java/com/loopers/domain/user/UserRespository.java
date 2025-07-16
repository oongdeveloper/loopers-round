package com.loopers.domain.user;

public interface UserRespository {
    public UserEntity find(String findId);

    public UserEntity save(UserEntity user);
}
