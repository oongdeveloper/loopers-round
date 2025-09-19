package com.loopers.batch.step;

import com.loopers.batch.step.writer.CustomRedisItemWriter;
import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.WeeklyRanking;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WeeklyRankingStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RedisCacheWrapper redisCacheWrapper;
    private final StepExecution stepExecution;

    @Bean
    public Step WeeklyRankingStep(ItemReader<WeeklyRanking> weeklyReader,
                                            ItemWriter<WeeklyRanking> weeklyWriter) {
        return new StepBuilder("weeklyRankingStep", jobRepository)
                .<WeeklyRanking, WeeklyRanking>chunk(1000, transactionManager)
                .reader(weeklyReader)
                .writer(weeklyWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<WeeklyRanking> weeklyReader(
                                                          EntityManagerFactory entityManagerFactory) {

        String targetWeek = stepExecution.getJobExecution().getExecutionContext().getString("targetWeek");
        Map<String, Object> params = new HashMap<>();
        params.put("metricDate", targetWeek);

        return new JpaPagingItemReaderBuilder<WeeklyRanking>()
                .name("weeklyRankingReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT d FROM WeeklyRanking d WHERE d.metricDate = :metricDate order by score")
                .parameterValues(params)
                .pageSize(1000)
                .build();
    }

    @Bean
    @StepScope
    public CustomRedisItemWriter<WeeklyRanking> weeklyWriter() {
        String targetWeek = stepExecution.getJobExecution().getExecutionContext().getString("targetWeek");
        return new CustomRedisItemWriter<WeeklyRanking>(redisCacheWrapper, targetWeek);
    }
}
