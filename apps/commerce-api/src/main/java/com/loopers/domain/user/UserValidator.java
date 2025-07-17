package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Transient;

public class UserValidator {

    @Transient
    private static final String USER_ID_PATTERN = "^[0-9a-zA-Z]{1,10}$";
    @Transient
    private static final String BIRTH_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    @Transient
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static void validate(UserCommand userCommand){
        if (userCommand.getUserId() == null || !userCommand.getUserId().matches(USER_ID_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "ID 는 1자 이상 10자 이내의 영문과 숫자로 이루어져야 합니다.");

        if (userCommand.getBirth() == null || !userCommand.getBirth().matches(BIRTH_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 맞지 않습니다. f) yyyy-MM-dd");

        if (userCommand.getEmail() == null || !userCommand.getEmail().matches(EMAIL_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다. ex) aa@bb.cc");
    }
}
