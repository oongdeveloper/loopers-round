package com.loopers.domain.ranking;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mv_weekly_ranking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class WeeklyRanking extends BaseAuditableEntity implements Ranking {
    @EmbeddedId
    private WeeklyId id;

//    @Setter
//    @Column(nullable = false)
//    private int rank;

    @Setter
    @Column(nullable = false)
    private Double score;

    private WeeklyRanking(WeeklyId id, Double score) {
        this.id = id;
        this.score = score;
    }

    public static WeeklyRanking of(String period, Long productId, Double score) {
        return new WeeklyRanking(
                WeeklyId.of(period, productId),
                score
        );
    }

    @Override
    public Long getProductId() {
        return this.id.productId;
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class WeeklyId{

        @Column(nullable = false)
        private String period;

        @Column(nullable = false)
        private Long productId;

        private WeeklyId(String period, Long productId) {
            this.period = period;
            this.productId = productId;
        }

        public static WeeklyId of(String period, Long productId) {
            return new WeeklyId(period, productId);
        }
    }

}
