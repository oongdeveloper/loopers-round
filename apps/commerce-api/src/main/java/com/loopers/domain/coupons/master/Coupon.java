package com.loopers.domain.coupons.master;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon extends BaseEntity {
    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private CouponType type;

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

    public enum CouponType{
        FIXED,
        RATE
    }
}
