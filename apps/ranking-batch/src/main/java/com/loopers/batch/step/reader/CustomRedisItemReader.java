package com.loopers.batch.step.reader;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.DailyRanking;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CustomRedisItemReader<T> extends AbstractItemCountingItemStreamItemReader {
    private final RedisCacheWrapper redisCacheWrapper;
    private final String targetDateStr;
    private final String key;
    private final int chunkSize;
    private final String RANKING_KEY = "ranking:product:";

    private Iterator<T> dataIterator;

    public CustomRedisItemReader(RedisCacheWrapper redisCacheWrapper, String targetDateStr,int chunkSize) {
        this.redisCacheWrapper = redisCacheWrapper;
        this.targetDateStr = targetDateStr;
        this.chunkSize = chunkSize;
        this.key = RANKING_KEY + targetDateStr;
    }

    @Override
    protected void doOpen() throws Exception {
        Assert.notNull(redisCacheWrapper, "RedisTemplate must not be null");
        Assert.notNull(key, "Key must not be null");

        Long totalItems = redisCacheWrapper.count(key);
        if (totalItems == null || totalItems == 0) {
            this.dataIterator = null;
        }
    }

    @Override
    protected T doRead() throws Exception {
        if (dataIterator == null || !dataIterator.hasNext()) {
            // 다음 청크를 읽어오기 위한 offset 계산 -> DB 에 있는 Read_Count 를 가져옴.
            int offset = getCurrentItemCount();

            // ZSET에서 지정된 범위의 데이터를 가져옴
            Map<Long, Float> dataSet = redisCacheWrapper.getRangeWithScore(key, offset, offset + chunkSize - 1);

            if (dataSet == null || dataSet.isEmpty()) {
                return null; // 더 이상 읽을 데이터가 없으면 null 반환
            }

            // Redis에서 가져온 데이터를 랭킹과 함께 가공
            List<DailyRanking> rankingDataList = new ArrayList<>();
            int rank = offset + 1; // 랭킹은 1부터 시작하므로 +1
            for (Map.Entry<Long, Float> data : dataSet.entrySet()) {
                Long productId = data.getKey();
                Double score = data.getValue().doubleValue();
                rankingDataList.add(DailyRanking.of(targetDateStr, productId, rank, score));
                rank++;
            }

            this.dataIterator = (Iterator<T>) rankingDataList.iterator();
//            this.dataIterator = (Iterator<T>) dataSet.entrySet().iterator();
        }

        return dataIterator.next();
    }

    @Override
    protected void doClose() throws Exception {
        this.dataIterator = null;
    }
}
