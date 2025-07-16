package com.loopers.interfaces.api.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserV1Dto {

    enum Gender{
        M,
        F
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
    }
}
