package com.loopers.data.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;
    private final ErrorDecoder errorDecoder = new Default();

    public CustomErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status());

        if (httpStatus == HttpStatus.BAD_REQUEST) {
            try {
                BadReqErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), BadReqErrorResponse.class);
                return new ValidateParamException(errorResponse.meta().errorCode(), errorResponse.meta().message());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            return new RetryableException();
        }
        return errorDecoder.decode(methodKey, response);
    }
}
