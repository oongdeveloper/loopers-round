package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.WeeklyRanking;
import com.loopers.domain.ranking.WeeklyRankingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WeeklyRankingRepositoryImpl implements WeeklyRankingRepository {
    private final WeeklyRankingJpaRepository jpaRepository;

    public WeeklyRankingRepositoryImpl(WeeklyRankingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<WeeklyRanking> findByProductIdAndPeriod(Long productId, String period) {
        return jpaRepository.findById(WeeklyRanking.WeeklyId.of(period, productId));
    }
}
