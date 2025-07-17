package com.loopers.domain.user;


import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserCommand {
    private String userId;
    private String userName;
    private UserEntity.Gender gender;
    private String birth;
    private String email;

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
