package com.loopers.application.user;

import com.loopers.domain.user.UserEntity;

public record UserInfo (
    String userId,
    String userName,
    UserEntity.Gender gender,
    String birth,
    String email
){
    public static UserInfo of(UserEntity entity) {
        return new UserInfo(
                entity.getUserId(),
                entity.getName(),
                entity.getGender(),
                entity.getBirth(),
                entity.getEmail()
        );
    }

}
