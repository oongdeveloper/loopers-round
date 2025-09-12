package com.loopers.domain.ranking;

import java.util.List;

public interface RankingWeightRepository {
    List<RankingWeight> getWeights();

    void addWeight(String key, Float weight);
}
