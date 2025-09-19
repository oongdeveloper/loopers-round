package com.loopers.batch.step.processor;

import com.loopers.domain.ranking.DailyRanking;
import com.loopers.domain.ranking.MonthlyRanking;
import com.loopers.domain.ranking.MonthlyRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonthlyScoreCalculateProcessor implements ItemProcessor<DailyRanking, MonthlyRanking> {
    private final MonthlyRankingRepository monthlyRankingRepository;
    private final StepExecution stepExecution;
    private static final double ALPHA = 0.5; // 지수가중평균 가중치

    @Override
    public MonthlyRanking process(DailyRanking dailyRanking) {
        String monthlyPeriod = stepExecution.getJobExecution().getExecutionContext().getString("targetMonth");
//        String monthlyPeriod = getMonthlyPeriod(dailyRanking.getId().getPeriod());

        Optional<MonthlyRanking> existingMonthlyRanking = monthlyRankingRepository.findByProductIdAndPeriod(
                dailyRanking.getId().getProductId(), monthlyPeriod);

        double newScore = dailyRanking.getScore();
        if (existingMonthlyRanking.isPresent()) {
            MonthlyRanking monthlyRanking = existingMonthlyRanking.get();
            double oldScore = monthlyRanking.getScore();
            newScore = (ALPHA * newScore) + ((1 - ALPHA) * oldScore);
            monthlyRanking.setScore(newScore);

            log.info("Updating existing WeeklyRanking for productId: {} with new score: {}", dailyRanking.getId().getProductId(), newScore);
            return monthlyRanking;
        } else {
            MonthlyRanking newMonthlyRanking = MonthlyRanking.of(
                    monthlyPeriod, dailyRanking.getId().getProductId(), newScore
            );
            log.info("Creating new WeeklyRanking for productId: {} with score: {}", dailyRanking.getId().getProductId(), newScore);
            return newMonthlyRanking;
        }
    }

    private String getMonthlyPeriod(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        YearMonth yearMonth = YearMonth.from(date);
        return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

}
