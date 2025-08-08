package com.loopers.domain.coupons.issued;


import com.loopers.domain.BaseEntity;
import com.loopers.domain.coupons.master.Coupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_coupon", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_coupon", columnNames = {"ref_user_id","ref_coupon_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCoupon extends BaseEntity {

    @Column(name = "ref_user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "ref_coupon_id", nullable = false, updatable = false)
    private Long couponId;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private ZonedDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private ZonedDateTime expiresAt;

    // TODO. boolean 의 표현?
    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "used_at")
    private ZonedDateTime usedAt;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private Coupon.CouponType type;

    @Column(name = "scope", length = 15)
    private String scope;

    @Column(name = "avaliable_target_id")
    private Long avaliableTargetId;

    @Column(name = "discount_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "max_discount_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal maxDiscountValue;

    @Column(name = "min_order_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal minOrderAmount;

    private UserCoupon(Long userId, Long couponId, ZonedDateTime expiresAt,
                      String name, Coupon.CouponType type,
                      String scope, Long avaliableTargetId,
                      BigDecimal maxDiscountValue, BigDecimal discountValue, BigDecimal minOrderAmount) {


        Objects.requireNonNull(userId, "쿠폰 UserId 는 null일 수 없습니다.");
        Objects.requireNonNull(couponId, "쿠폰 Id는 null일 수 없습니다.");
        Objects.requireNonNull(expiresAt, "만료 일시는 null일 수 없습니다.");
        Objects.requireNonNull(type, "쿠폰 타입은 null일 수 없습니다.");
        Objects.requireNonNull(discountValue, "할인 금액은 null일 수 없습니다.");

        if (type.equals(Coupon.CouponType.RATE)) {
            if (discountValue.compareTo(new BigDecimal(100L)) > 0) {
                throw new IllegalArgumentException("정률 할인율은 100을 초과할 수 없습니다.");
            }
        }

        validateMoneyValue(maxDiscountValue, "최대 할인 금액");
        validateMoneyValue(minOrderAmount, "최소 주문 금액");

        this.userId = userId;
        this.couponId = couponId;;
        this.expiresAt = expiresAt;
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.avaliableTargetId = avaliableTargetId;
        this.maxDiscountValue = maxDiscountValue;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;

        this.issuedAt = ZonedDateTime.now();
    }

    public static UserCoupon of(
            Long userId, Long couponId, ZonedDateTime expiresAt,
            String name, Coupon.CouponType type,
            String scope, Long avaliableTargetId,
            BigDecimal maxDiscountValue, BigDecimal discountValue, BigDecimal minOrderAmount) {

        return new UserCoupon(
                userId, couponId, expiresAt,
                name, type,
                scope, avaliableTargetId,
                maxDiscountValue, discountValue, minOrderAmount
        );
    }


    public void use() {
        if (this.isUsed) {
            throw new IllegalStateException("이미 사용된 쿠폰입니다. " + this.name);
        }
        this.isUsed = true;
        this.usedAt = ZonedDateTime.now();
    }

    private void validateMoneyValue(BigDecimal amount, String fieldName) {
        BigDecimal MAX_MONEY_LIMIT = BigDecimal.valueOf(10_000_000_000L);
        if (amount != null) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(fieldName + "은(는) 음수일 수 없습니다.");
            }
            if (amount.compareTo(MAX_MONEY_LIMIT) > 0) {
                throw new IllegalArgumentException(fieldName + "은(는) 100억을 초과할 수 없습니다.");
            }
        }
    }
}
