package com.loopers.domain.coupons.issued.policy;

import com.loopers.domain.coupons.master.Coupon;

import java.math.BigDecimal;

public record DiscountInfo(
        BigDecimal discountValue,
        BigDecimal maxDiscountValue,
        DiscountType discountType
) {
    public static DiscountInfo of(BigDecimal discountValue, BigDecimal maxDiscountValue, Coupon.CouponType type) {
        return new DiscountInfo(
                discountValue,
                maxDiscountValue,
                convertToDiscountInfoType(type)
        );
    }

    private static DiscountType convertToDiscountInfoType(Coupon.CouponType couponType) {
        switch (couponType) {
            case FIXED:
                return DiscountType.FIXED;
            case RATE:
                return DiscountType.RATE;
            default:
                throw new IllegalArgumentException("지원하지 않는 쿠폰 할인 유형입니다: " + couponType);
        }
    }

    public enum DiscountType {
        FIXED,
        RATE
    }

}
