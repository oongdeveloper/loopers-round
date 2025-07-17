package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserV1Dto {

    public enum Gender{
        M,
        F;
        private static Gender stringToEnum(String genderStr) {
            try{
                return Gender.valueOf(genderStr);
            } catch (IllegalArgumentException e){
                log.error("존재하지 않는 성별입니다. Gender = {}.", genderStr);
                throw new CoreException(ErrorType.INTERNAL_ERROR, "정상적이지 않은 User 입니다.");
            }
        }
    }

    public record SignUpRequest(
            @NotNull(message = "빈 값일 수 없습니다.")
            @Pattern(regexp = "^[0-9a-zA-Z]{1,10}$", message = "ID 는 1자 이상 10자 이내의 영문과 숫자로 이루어져야 합니다.")
            String userId,
            @NotNull(message = "빈 값일 수 없습니다.")
            String userName,
            @NotNull(message = "빈 값일 수 없습니다.")
            Gender gender,
            @NotNull(message = "빈 값일 수 없습니다.")
            @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 맞지 않습니다. f) yyyy-MM-dd")
            String birth,
            @NotNull(message = "빈 값일 수 없습니다.")
            @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다. ex) aa@bb.cc")
            String email
    ) {
    }

    public record ChargePointRequest(
        long point
    ) {
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
                    Gender.stringToEnum(userInfo.gender().name()),
                    userInfo.birth(),
                    userInfo.email()
            );
        }
    }

    public record UserPointResponse(
            String userId,
            long point
    ){}
}

