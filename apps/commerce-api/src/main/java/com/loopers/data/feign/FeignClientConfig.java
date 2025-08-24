package com.loopers.data.feign;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Request.Options feignOptions(){
        return new Request.Options(300, 1000);
    }

    @Bean
    public CustomErrorDecoder customErrorDecorder(ObjectMapper objectMapper) {
        return new CustomErrorDecoder(objectMapper);
    }
}
