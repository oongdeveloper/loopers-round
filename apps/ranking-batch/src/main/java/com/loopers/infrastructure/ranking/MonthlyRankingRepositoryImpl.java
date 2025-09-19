package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.MonthlyRanking;
import com.loopers.domain.ranking.MonthlyRankingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MonthlyRankingRepositoryImpl implements MonthlyRankingRepository {
    private final MonthlyRankingJpaRepository jpaRepository;

    public MonthlyRankingRepositoryImpl(MonthlyRankingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<MonthlyRanking> findByProductIdAndPeriod(Long productId, String period) {
        return jpaRepository.findById(MonthlyRanking.MonthlyId.of(period, productId));
    }
}
