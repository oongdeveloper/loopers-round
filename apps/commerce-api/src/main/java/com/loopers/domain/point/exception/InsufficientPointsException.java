package com.loopers.domain.point.exception;

public class InsufficientPointsException extends RuntimeException{
    public InsufficientPointsException(String message) {
        super(message);
    }

    public InsufficientPointsException(String message, Throwable cause) {
        super(message, cause);
    }
}
