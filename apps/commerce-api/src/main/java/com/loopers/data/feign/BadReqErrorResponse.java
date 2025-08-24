package com.loopers.data.feign;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BadReqErrorResponse (
        @JsonProperty("meta") Meta meta
){
    protected record Meta(
            String result,
            String errorCode,
            String message
    ){}
}
