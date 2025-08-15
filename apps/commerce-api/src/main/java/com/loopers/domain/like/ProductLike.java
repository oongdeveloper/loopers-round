package com.loopers.domain.like;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "product_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductLike extends BaseAuditableEntity {

    @Id
    @Column(name = "ref_product_id", nullable = false)
    private Long productId;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    private ProductLike(Long productId, Long likeCount) {
        Objects.requireNonNull(productId, "제품ID 는 필수값입니다.");

        this.productId = productId;
        this.likeCount = likeCount;
    }

    public static ProductLike of(Long productId, Long likeCount) {
        return new ProductLike(productId, likeCount);
    }
}
