package com.loopers.interfaces.api;

import java.util.Map;

public record ApiResponse<T>(Metadata meta, T data, Map<String, String> errors) {
    public ApiResponse{
        errors = errors;
    }

    public record Metadata(Result result, String errorCode, String message) {
        public enum Result {
            SUCCESS, FAIL
        }

        public static Metadata success() {
            return new Metadata(Result.SUCCESS, null, null);
        }

        public static Metadata fail(String errorCode, String errorMessage) {
            return new Metadata(Result.FAIL, errorCode, errorMessage);
        }
    }

    public static ApiResponse<Object> success() {
        return new ApiResponse<>(Metadata.success(), null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Metadata.success(), data, null);
    }

    public static ApiResponse<Object> fail(String errorCode, String errorMessage) {
        return new ApiResponse<>(
            Metadata.fail(errorCode, errorMessage),
            null, null
        );
    }

    public static ApiResponse<Object> fail(String errorCode, String errorMessage, Map errors) {
        return new ApiResponse<>(
                Metadata.fail(errorCode, errorMessage),
                null,
                errors
        );
    }

}
