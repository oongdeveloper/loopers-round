package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserV1Dto {

    enum Gender{
        M,
        F;

        // TODO. 이거 변환 어떻게 함?
        private static Gender fromString(String genderStr) {
            for (Gender g : Gender.values()) {
                if (g.name().equalsIgnoreCase(genderStr)) { // 대소문자 무시 비교
                    return g;
                }
            }
            throw new IllegalArgumentException("Invalid Gender string for UserV1Dto: " + genderStr);
        }
    }

    public record SignUpRequest(
            @NotNull
            String userId,
            @NotNull
            String name,
            @NotNull
            Gender gender,
            @NotNull
            String birth,
            @NotNull
            String email
    ) {
//        // TODO. 여기다 검증을 두는 게 맞을까?
//        public SignUpRequest{
//            if (gender == null) {
//                throw new CoreException(ErrorType.BAD_REQUEST,"성별은 빈 값일 수 없습니다.");
//            }
//        }
//        enum GenderRespone{
//            M,F
//        }
    }

    public record UserResponse (
        String userId,
        String name,
        Gender gender,
        String birth,
        String email
    ){
        public static UserResponse from(UserInfo userInfo) {
            return new UserResponse(
                    userInfo.userId(),
                    userInfo.userName(),
                    Gender.fromString(userInfo.gender().name()),
                    userInfo.birth(),
                    userInfo.email()
            );
        }
    }
}

