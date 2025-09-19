package com.loopers.domain.ranking;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mv_daily_ranking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class DailyRanking {
    @EmbeddedId
    private DailyId id;

    @Column(nullable = false)
    private int rank;
    @Column(nullable = false)
    private Double Score;

    private DailyRanking(DailyId id, int rank, Double score) {
        this.id = id;
        this.rank = rank;
        Score = score;
    }

    public static DailyRanking of(String period, Long productId, int rank, Double score) {
        return new DailyRanking(DailyId.of(period, productId), rank, score);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class DailyId{

        @Column(nullable = false)
        private String period;

        @Column(nullable = false)
        private Long productId;

        private DailyId(String period, Long productId) {
            this.period = period;
            this.productId = productId;
        }

        public static DailyId of(String period, Long productId){
            return new DailyId(period, productId);
        }

    }
}
