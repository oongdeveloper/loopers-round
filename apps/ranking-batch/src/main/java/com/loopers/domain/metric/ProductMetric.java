package com.loopers.domain.metric;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "product_metrics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductMetric extends BaseAuditableEntity {
    @EmbeddedId
    private MetricId id;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "sales_volume")
    private Long salesVolume;

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class MetricId {
        @Column(name = "product_id", nullable = false, unique = true)
        private Long productId;

        @Column(name = "aggregation_date")
        private LocalDate aggregationDate;

        private MetricId(Long productId, LocalDate aggregationDate){
            this.productId = productId;
            this.aggregationDate = aggregationDate;
        }
    }
}
