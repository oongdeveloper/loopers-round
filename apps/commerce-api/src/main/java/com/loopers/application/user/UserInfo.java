package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo (
    String userId,
    String userName,
    User.Gender gender,
    String birth,
    String email
){
    public static UserInfo from(User entity) {
        return new UserInfo(
                entity.getUserId(),
                entity.getUserName(),
                entity.getGender(),
                entity.getBirth(),
                entity.getEmail()
        );
    }

}
