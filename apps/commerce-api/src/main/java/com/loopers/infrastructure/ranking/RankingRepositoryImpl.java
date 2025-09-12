package com.loopers.infrastructure.ranking;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.RankingRepository;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class RankingRepositoryImpl implements RankingRepository {
    private final RedisCacheWrapper redisCacheWrapper;

    public RankingRepositoryImpl(RedisCacheWrapper redisCacheWrapper) {
        this.redisCacheWrapper = redisCacheWrapper;
    }

    @Override
    public Set<Long> getRange(String key, int start, int end){
        return redisCacheWrapper.getRange(key, start, end);
    }
    @Override
    public Map<Long, Float> getRangeWithScore(String key, int start, int end){
        return redisCacheWrapper.getRangeWithScore(key, start, end);
    }

    @Override
    public void add(String key, Map<Long, Float> map){
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (Map.Entry<Long, Float> entry : map.entrySet()) {
            String member = String.valueOf(entry.getKey());   // 맵의 키가 ZSET의 멤버(String)
            Double score = Double.valueOf(entry.getValue());  // 맵의 값이 ZSET의 스코어(Double)

            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>(member, score);
            tuples.add(tuple);
        }
        redisCacheWrapper.addZset(key, tuples);
    }
}
