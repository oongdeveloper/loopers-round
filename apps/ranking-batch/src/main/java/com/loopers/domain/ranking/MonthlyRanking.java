package com.loopers.domain.ranking;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "mv_monthly_ranking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class MonthlyRanking extends BaseAuditableEntity implements Ranking {
    @EmbeddedId
    private MonthlyId id;

    @Setter
    @Column(nullable = false)
    private Double score;


    private MonthlyRanking(MonthlyId id, Double score) {
        this.id = id;
        this.score = score;
    }

    public static MonthlyRanking of(String period, Long productId, Double score) {
        return new MonthlyRanking(
                MonthlyId.of(period, productId),
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
    public static class MonthlyId{

        @Column(nullable = false)
        private String period;

        @Column(nullable = false)
        private Long productId;

        private MonthlyId(String period, Long productId) {
            this.period = period;
            this.productId = productId;
        }

        public static MonthlyId of(String period, Long productId) {
            return new MonthlyId(period, productId);
        }
    }

}
