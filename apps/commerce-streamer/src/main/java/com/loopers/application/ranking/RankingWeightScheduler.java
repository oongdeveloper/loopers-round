package com.loopers.application.ranking;

import com.loopers.domain.ranking.RankingWeight;
import com.loopers.domain.ranking.RankingWeightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RankingWeightScheduler {
    private final RankingWeightService rankingWeightService;
    private final String VIEW_WEIGHT_KEY = "ranking:weight:view";
    private final String LIKE_WEIGHT_KEY = "ranking:weight:like";
    private final String ORDER_WEIGHT_KEY = "ranking:weight:order";

    public RankingWeightScheduler(RankingWeightService rankingWeightService) {
        this.rankingWeightService = rankingWeightService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void refreshWeight(){
        List<RankingWeight> weights = rankingWeightService.getWeights();

        for (RankingWeight weight : weights) {
            rankingWeightService.addWeight(weight.getName(), weight.getWeight());
        }
    }
}
