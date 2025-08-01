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

    private Like(Long userId, Long productCatalogId) {
        this.id = new LikeId(userId, productCatalogId);
    }

    public static Like of(Long userId, Long productCatalogId) {
        return new Like(userId, productCatalogId);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class LikeId {
        @Column(name = "ref_user_id", nullable = false)
        Long userId;

        @Column(name = "ref_product_catalog_id", nullable = false)
        Long productCatalogId;

        private LikeId(Long userId, Long productCatalogId){
            this.userId = userId;
            this.productCatalogId = productCatalogId;
        }

        public static LikeId of(Long userId, Long productCatalogId) {
            return new LikeId(userId, productCatalogId);
        }
    }
}
