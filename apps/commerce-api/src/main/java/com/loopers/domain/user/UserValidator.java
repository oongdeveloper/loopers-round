package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class UserValidator {
    private static final String USER_ID_PATTERN = "^[0-9a-zA-Z]{1,10}$";
    private static final String BIRTH_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static void validate(UserCommand userCommand){
        validateUserId(userCommand.userId());
        validateBirth(userCommand.birth());
        validateEmail(userCommand.email());
    }

    private static void validateUserId(String userId){
        if (userId == null || !userId.matches(USER_ID_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "ID 는 1자 이상 10자 이내의 영문과 숫자로 이루어져야 합니다.");
    }

    private static void validateBirth(String birth){
        if (birth == null || !birth.matches(BIRTH_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 맞지 않습니다. f) yyyy-MM-dd");
        // TODO. 오늘 이전 날짜는 에러 발생.
//        if (LocalDate.now().isBefore(LocalDate.parse(birthDate))) {
//            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 오늘 이전이어야 합니다: " + birthDate);
//        }
    }

    private static void validateEmail(String email){
        if (email == null || !email.matches(EMAIL_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다. ex) aa@bb.cc");
    }
}

