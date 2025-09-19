package com.loopers.batch.step;

import com.loopers.batch.step.reader.CustomRedisItemReader;
import com.loopers.config.redis.RedisCacheWrapper;
import com.loopers.domain.ranking.DailyRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DailyRankingStepConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step dailyRankingStep(ItemReader<DailyRanking> dailyReader,
                                 ItemWriter<DailyRanking> dailyWriter) {
        return new StepBuilder("dailyRankingStep", jobRepository)
                .<DailyRanking, DailyRanking>chunk(1000, transactionManager)
                .reader(dailyReader)
                .writer(dailyWriter)
                .build();
    }

    @Bean
    @StepScope
    public CustomRedisItemReader<DailyRanking> dailyReader(@Value("#{jobParameters[targetDate]}") String targetDateStr,
                                                           RedisCacheWrapper redisCache,
                                                           @Value("#{stepExecutionContext['chunkSize'] ?: 1000}")  int chunkSize) {
        return new CustomRedisItemReader<DailyRanking>(redisCache, targetDateStr, chunkSize);
    }

    @Bean
    @StepScope
    public ItemWriter<DailyRanking> dailyWriter(
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        final String INSERT_SQL =
                "INSERT INTO mv_daily_ranking (period, product_id, rank, score) VALUES (:period, :productId, :rank, :score)";

        return new JdbcBatchItemWriterBuilder<DailyRanking>()
                .namedParametersJdbcTemplate(jdbcTemplate)
                .sql(INSERT_SQL)
                .itemSqlParameterSourceProvider(item -> {
                    MapSqlParameterSource source = new MapSqlParameterSource();
                    source.addValue("productId", item.getId().getProductId());
                    source.addValue("period", item.getId().getPeriod());
                    source.addValue("score", item.getScore());
                    source.addValue("rank", item.getRank());
                    return source;
                })
                .build();
    }
}
