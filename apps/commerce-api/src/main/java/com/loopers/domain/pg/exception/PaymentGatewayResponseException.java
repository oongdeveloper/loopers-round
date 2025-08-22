package com.loopers.domain.pg.exception;

public class PaymentGatewayResponseException extends Exception{

    public PaymentGatewayResponseException(String message) {
        super(message);
    }

    public PaymentGatewayResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
