package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.RankingWeight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingWeightJpaRepository extends JpaRepository<RankingWeight, String> {
}
