package com.loopers.domain.coupons.issued.policy;

import com.loopers.domain.coupons.issued.UserCoupon;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CouponCalculator {
    private final DiscountPolicyFactory discountPolicyFactory;

    public CouponCalculator(DiscountPolicyFactory discountPolicyFactory) {
        this.discountPolicyFactory = discountPolicyFactory;
    }

    public BigDecimal applyCoupon(UserCoupon coupon, BigDecimal amount) {
        DiscountInfo discountInfo = DiscountInfo.of(
                coupon.getDiscountValue(),
                coupon.getMaxDiscountValue(),
                coupon.getType()
        );

        DiscountPolicy discountPolicy = discountPolicyFactory.getPolicy(discountInfo.discountType());
        return discountPolicy.applyDiscount(amount, discountInfo);
    }
}
