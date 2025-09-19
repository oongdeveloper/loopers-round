package com.loopers.batch.step;

import com.loopers.batch.step.writer.CustomRedisItemWriter;
import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.MonthlyRanking;
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
public class MonthlyRankingStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RedisCacheWrapper redisCacheWrapper;
    private final StepExecution stepExecution;

    @Bean
    public Step MonthlyRankingStep(ItemReader<MonthlyRanking> monthlyReader,
                                            ItemWriter<MonthlyRanking> monthlyWriter) {
        return new StepBuilder("monthlyRankingStep", jobRepository)
                .<MonthlyRanking, MonthlyRanking>chunk(1000, transactionManager)
                .reader(monthlyReader)
                .writer(monthlyWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MonthlyRanking> monthlyReader(
                                                          EntityManagerFactory entityManagerFactory) {

        String targetWeek = stepExecution.getJobExecution().getExecutionContext().getString("targetMonth");
        Map<String, Object> params = new HashMap<>();
        params.put("metricDate", targetWeek);

        return new JpaPagingItemReaderBuilder<MonthlyRanking>()
                .name("monthlyRankingReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT d FROM MonthlyRanking d WHERE d.metricDate = :metricDate order by score")
                .parameterValues(params)
                .pageSize(1000)
                .build();
    }

    @Bean
    @StepScope
    public CustomRedisItemWriter<MonthlyRanking> monthlyWriter() {
        String targetMonth = stepExecution.getJobExecution().getExecutionContext().getString("targetMonth");
        return new CustomRedisItemWriter<MonthlyRanking>(redisCacheWrapper, targetMonth);
    }
}
