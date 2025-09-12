package com.loopers.domain.ranking;

import java.util.Map;

public interface RankingRepository {
    void add(String key, Map<String, Double> map);

    Float getWeight(String key);
}
