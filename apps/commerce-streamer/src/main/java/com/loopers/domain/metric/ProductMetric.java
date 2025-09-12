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

    // 상품 좋아요 카운트
    @Column(name = "like_count")
    private Long likeCount;

    // 상품 판매량
    @Column(name = "sales_volume")
    private Long salesVolume;

    public ProductMetric(Long id, LocalDate date) {
        this.id = MetricId.of(id, date);
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.salesVolume = 0L;
    }

    public static ProductMetric of(Long id, LocalDate date){
        return new ProductMetric(id, date);
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }

    public void increaseSaleCount(){
        this.salesVolume++;
    }

    public void decreaseSaleCount(){
        this.salesVolume--;
    }

    public void increaseViewCount(long count){
        this.viewCount += count;
    }

    public void increaseLikeCount(long count){
        this.likeCount += count;
    }

    public void decreaseLikeCount(long count){
        this.likeCount -= count;
    }

    public void increaseSaleCount(long count){
        this.salesVolume += count;
    }

    public void decreaseSaleCount(long count){
        this.salesVolume -= count;
    }


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

        public static MetricId of(Long productId, LocalDate aggregationDate) {
            return new MetricId(productId, aggregationDate);
        }
    }
}
