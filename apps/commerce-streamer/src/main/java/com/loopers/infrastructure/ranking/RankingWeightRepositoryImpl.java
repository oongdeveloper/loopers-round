package com.loopers.infrastructure.ranking;

import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.RankingWeight;
import com.loopers.domain.ranking.RankingWeightRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingWeightRepositoryImpl implements RankingWeightRepository {
    private final RankingWeightJpaRepository jpaRepository;
    private final RedisCacheWrapper redisCacheWrapper;

    public RankingWeightRepositoryImpl(RankingWeightJpaRepository jpaRepository, RedisCacheWrapper redisCacheWrapper) {
        this.jpaRepository = jpaRepository;
        this.redisCacheWrapper = redisCacheWrapper;
    }

    @Override
    public List<RankingWeight> getWeights() {
        return jpaRepository.findAll();
    }

    @Override
    public void addWeight(String key, Float weight) {
        redisCacheWrapper.set(key, weight);
    }
}
