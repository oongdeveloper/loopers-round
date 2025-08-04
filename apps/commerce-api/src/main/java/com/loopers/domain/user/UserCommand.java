package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record UserCommand (
        String userId,
        String userName,
        User.Gender gender,
        String birth,
        String email
) {
    public static UserCommand of(String userId, String userName, Enum<?> gender, String birth, String email){
        return new UserCommand(
                userId, userName, stringToEnum(gender.name()), birth, email
        );
    }

    private static User.Gender stringToEnum(String genderStr){
        try{
            return User.Gender.valueOf(genderStr);
        } catch (IllegalArgumentException e){
            log.error("존재하지 않는 성별입니다. Gender = {}.", genderStr);
            throw new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 성별입니다.");
        }
    }
}
