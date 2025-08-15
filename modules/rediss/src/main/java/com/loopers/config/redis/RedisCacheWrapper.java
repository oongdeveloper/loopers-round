package com.loopers.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisCacheWrapper {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheWrapper(@Qualifier("defaultRedisTemplate") RedisTemplate<String, String> redisTemplate
                                , ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Redis에 객체를 JSON 문자열로 저장합니다.
     *
     * @param key   저장할 Redis 키
     * @param value 저장할 객체
     * @param <T>   객체 타입
     */
    public <T> void set(String key, T value) {
        try {
            // 객체를 JSON 문자열로 변환
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (JsonProcessingException e) {
            // 직렬화 실패 시 예외 처리
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Redis에 객체를 JSON 문자열로 저장하며 TTL을 설정합니다.
     *
     * @param key      저장할 Redis 키
     * @param value    저장할 객체
     * @param timeout  TTL 시간
     * @param unit     TTL 시간 단위
     * @param <T>      객체 타입
     */
    public <T> void set(String key, T value, long timeout, TimeUnit unit) {
        try {
            // 객체를 JSON 문자열로 변환
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeout, unit);
        } catch (JsonProcessingException e) {
            // 직렬화 실패 시 예외 처리
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }


    /**
     * 특정 키에 해당하는 데이터를 Redis에서 삭제합니다.
     * @param key 삭제할 키
     */
    public void delete(String key) {
        Boolean result = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(result)) {
            log.info("Key '" + key + "' was deleted successfully.");
        } else {
            log.info("Key '" + key + "' was not found or could not be deleted.");
        }
    }

    /**
     * Redis에서 JSON 문자열을 가져와 객체로 역직렬화합니다.
     *
     * @param key        조회할 Redis 키
     * @param valueType  역직렬화할 객체의 클래스 타입
     * @param <T>        객체 타입
     * @return 역직렬화된 객체, 키가 없으면 null 반환
     */
    public <T> T get(String key, Class<T> valueType) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue == null) {
            return null;
        }

        try {
            // JSON 문자열을 객체로 변환
            return objectMapper.readValue(jsonValue, valueType);
        } catch (IOException e) {
            // 역직렬화 실패 시 예외 처리
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }

    /**
     * Redis에서 JSON 문자열을 가져와 제네릭 타입의 객체로 역직렬화합니다. (컬렉션 타입용)
     *
     * @param key          조회할 Redis 키
     * @param typeReference 역직렬화할 제네릭 타입 정보
     * @param <T>          제네릭 타입
     * @return 역직렬화된 객체, 키가 없으면 null 반환
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonValue, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }


}
