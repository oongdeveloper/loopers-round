package com.loopers.domain.ranking;

import java.util.Optional;

public interface WeeklyRankingRepository {

    Optional<WeeklyRanking> findByProductIdAndPeriod(Long productId, String period);
}
