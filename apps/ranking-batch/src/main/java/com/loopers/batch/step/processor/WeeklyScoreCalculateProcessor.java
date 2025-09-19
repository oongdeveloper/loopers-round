package com.loopers.batch.step.processor;

import com.loopers.domain.ranking.DailyRanking;
import com.loopers.domain.ranking.WeeklyRanking;
import com.loopers.domain.ranking.WeeklyRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeeklyScoreCalculateProcessor implements ItemProcessor<DailyRanking, WeeklyRanking> {
    private final WeeklyRankingRepository weeklyRankingRepository;
    private static final double ALPHA = 0.5; // 지수가중평균 가중치

    @Override
    public WeeklyRanking process(DailyRanking dailyRanking) {
        String weeklyPeriod = getWeeklyPeriod(dailyRanking.getId().getPeriod());

        Optional<WeeklyRanking> existingWeeklyRanking = weeklyRankingRepository.findByProductIdAndPeriod(
                dailyRanking.getId().getProductId(), weeklyPeriod);

        double newScore = dailyRanking.getScore();
        if (existingWeeklyRanking.isPresent()) {
            WeeklyRanking weeklyRanking = existingWeeklyRanking.get();
            double oldScore = weeklyRanking.getScore();
            newScore = (ALPHA * newScore) + ((1 - ALPHA) * oldScore);
            weeklyRanking.setScore(newScore);

            log.info("Updating existing WeeklyRanking for productId: {} with new score: {}", dailyRanking.getId().getProductId(), newScore);
            return weeklyRanking;
        } else {
            WeeklyRanking newWeeklyRanking = WeeklyRanking.of(
                    weeklyPeriod, dailyRanking.getId().getProductId(), newScore
            );
            log.info("Creating new WeeklyRanking for productId: {} with score: {}", dailyRanking.getId().getProductId(), newScore);
            return newWeeklyRanking;
        }
    }

    private String getWeeklyPeriod(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        int year = date.get(weekFields.weekBasedYear());
        return String.format("%d-%02d", year, weekNumber);
    }
}
