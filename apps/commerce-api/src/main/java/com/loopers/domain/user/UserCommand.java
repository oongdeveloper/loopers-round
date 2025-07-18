package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserCommand {
    private final String userId;
    private final String userName;
    private final UserEntity.Gender gender;
    private final String birth;
    private final String email;

    public static UserCommand of(String userId, String userName, Enum<?> gender, String birth, String email){
        return new UserCommand(
                userId, userName, gender.name(), birth, email
        );
    }

    private UserCommand(String userId, String userName, String gender, String birth, String email) {
        this.userId = userId;
        this.userName = userName;
        this.gender = stringToEnum(gender);
        this.birth = birth;
        this.email = email;
    }

    private UserEntity.Gender stringToEnum(String genderStr){
        try{
            return UserEntity.Gender.valueOf(genderStr);
        } catch (IllegalArgumentException e){
            log.error("존재하지 않는 성별입니다. Gender = {}.", genderStr);
            throw new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 성별입니다.");
        }
    }
}
