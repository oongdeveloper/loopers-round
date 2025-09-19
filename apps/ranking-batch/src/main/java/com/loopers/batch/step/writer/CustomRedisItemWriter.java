package com.loopers.batch.step.writer;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.Ranking;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CustomRedisItemWriter<T> extends AbstractItemStreamItemWriter {
    private final RedisCacheWrapper redisCacheWrapper;
    private final String key;
    private final String RANKING_KEY = "ranking:product:";

    private Iterator<T> dataIterator;

    public CustomRedisItemWriter(RedisCacheWrapper redisCacheWrapper, String targetDateStr) {
        this.redisCacheWrapper = redisCacheWrapper;
        this.key = RANKING_KEY + targetDateStr;
    }

    @Override
    public void write(Chunk chunk) throws Exception {
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (Object item : chunk.getItems()) {
            Ranking ranking = (Ranking) item;
            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>(
                    ranking.getProductId().toString(), ranking.getScore());
            tuples.add(tuple);
        }
        redisCacheWrapper.addZset(key, tuples);
    }
}
