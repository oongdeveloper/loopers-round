package com.loopers.domain.like;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Like extends BaseAuditableEntity {
    @EmbeddedId
    private LikeId id;

    private Like(Long userId, Long productId) {
        this.id = new LikeId(userId, productId);
    }

    public static Like of(Long userId, Long productId) {
        return new Like(userId, productId);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class LikeId {
        @Column(name = "ref_user_id", nullable = false)
        Long userId;

        @Column(name = "ref_product_catalog_id", nullable = false)
        Long productId;

        private LikeId(Long userId, Long productId){
            this.userId = userId;
            this.productId = productId;
        }

        public static LikeId of(Long userId, Long productId) {
            return new LikeId(userId, productId);
        }
    }
}
