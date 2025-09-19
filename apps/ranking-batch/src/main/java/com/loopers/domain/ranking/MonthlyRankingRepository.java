package com.loopers.domain.ranking;

import java.util.Optional;

public interface MonthlyRankingRepository {
    Optional<MonthlyRanking> findByProductIdAndPeriod(Long productId, String period);
}
