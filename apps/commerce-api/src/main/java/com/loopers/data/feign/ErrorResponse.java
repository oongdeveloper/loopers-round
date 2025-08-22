package com.loopers.data.feign;

public record ErrorResponse (
        BusinessFeignException.Code code,
        String reason
){
}
