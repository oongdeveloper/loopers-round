package com.loopers.data.feign;

public class ValidateParamException extends RuntimeException {
    private final String errorCode;

    public ValidateParamException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
