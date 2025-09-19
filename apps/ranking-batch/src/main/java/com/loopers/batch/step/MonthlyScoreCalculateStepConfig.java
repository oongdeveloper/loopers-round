package com.loopers.batch.step;

import com.loopers.batch.step.processor.MonthlyScoreCalculateProcessor;
import com.loopers.domain.ranking.DailyRanking;
import com.loopers.domain.ranking.MonthlyRanking;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MonthlyScoreCalculateStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final MonthlyScoreCalculateProcessor processor;

    @Bean
    public Step MonthlyScoreCalculateStep(ItemReader<DailyRanking> monthlyScoreReader,
                                 ItemWriter<MonthlyRanking> monthlyScoreWriter) {
        return new StepBuilder("monthlyScoreCalculateStep", jobRepository)
                .<DailyRanking, MonthlyRanking>chunk(1000, transactionManager)
                .reader(monthlyScoreReader)
                .processor(processor)
                .writer(monthlyScoreWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<DailyRanking> monthlyScoreReader(@Value("#{jobParameters[targetDate]}") String targetDateStr,
                                                         EntityManagerFactory entityManagerFactory) {

        Map<String, Object> params = new HashMap<>();
        params.put("metricDate", targetDateStr);

        return new JpaPagingItemReaderBuilder<DailyRanking>()
                .name("dailyRankingReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT d FROM DailyRanking d WHERE d.metricDate = :metricDate order by rank")
                .parameterValues(params)
                .pageSize(1000)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<MonthlyRanking> monthlyScoreWriter(
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        final String INSERT_SQL =
                "INSERT INTO mv_monthly_ranking (period, product_id, score) VALUES (:period, :productId, :score)";

        return new JdbcBatchItemWriterBuilder<MonthlyRanking>()
                .namedParametersJdbcTemplate(jdbcTemplate)
                .sql(INSERT_SQL)
                .itemSqlParameterSourceProvider(item -> {
                    MapSqlParameterSource source = new MapSqlParameterSource();
                    source.addValue("productId", item.getId().getProductId());
                    source.addValue("period", item.getId().getPeriod());
                    source.addValue("score", item.getScore());
                    return source;
                })
                .build();
    }

//    @Bean
//    @StepScope
//    public JpaItemWriter<MonthlyRanking> monthlyScoreWriter() {
//        JpaItemWriter<MonthlyRanking> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
}
