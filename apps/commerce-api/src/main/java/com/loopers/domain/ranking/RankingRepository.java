package com.loopers.domain.ranking;

import java.util.Map;
import java.util.Set;

public interface RankingRepository {
    Set<Long> getRange(String key, int start, int end);

    Map<Long, Float> getRangeWithScore(String key, int start, int end);
    void add(String key, Map<Long, Float> map);
}
