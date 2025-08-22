package com.loopers.data.feign;

public class BusinessFeignException extends RuntimeException {
    private final Code errorCode;

    public BusinessFeignException(Code errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return errorCode.name();
    }

    public enum Code {
        FAILED, SUCCESS
    }
}
