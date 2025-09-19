package com.loopers.batch.job;


import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RankingJobConfig {
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private static final double ALPHA = 0.5; // 지수가중평균 가중치

    @Bean
    public Job rankingJob(
            Step dailyRankingStep,
            Step weeklyScoreCalculateStep,
            Step weeklyRankingStep,
            Step monthlyScoreCalculateStep,
            Step monthlyRankingStep
    ) {
        return new JobBuilder("rankingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dailyRankingStep)
                .next(weeklyScoreCalculateStep)
                .next(weeklyRankingStep)
                .next(monthlyScoreCalculateStep)
                .next(monthlyRankingStep)
                .build();
    }

}
