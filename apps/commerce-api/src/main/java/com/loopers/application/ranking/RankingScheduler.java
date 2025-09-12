package com.loopers.application.ranking;

import com.loopers.domain.ranking.RankingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RankingScheduler {
    private final RankingService rankingService;
    private final String RANKING_KEY = "ranking:product:";
    private final Float CARRY_OVER_WEIGHT = 0.1f;

    public RankingScheduler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void carryOver(){
        String curKey = RANKING_KEY + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nextKey = RANKING_KEY + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        int decimalPlaces = 2;
        float scaleFactor = (float) Math.pow(10, decimalPlaces); // 100.0

        Map<Long, Float> curRanking = rankingService.getRangeWithScore(curKey, 0, 1000);
        Map<Long, Float> nextRanking = curRanking.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            float score = entry.getValue();
                            long truncatedScore = (long)(score * scaleFactor);
                            return (float)truncatedScore / scaleFactor;
                        }
                ));

        rankingService.add(nextKey, nextRanking);
    }
}
