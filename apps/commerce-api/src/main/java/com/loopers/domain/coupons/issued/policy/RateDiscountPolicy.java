package com.loopers.domain.coupons.issued.policy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RateDiscountPolicy implements DiscountPolicy{
    private final BigDecimal HUNDRED = BigDecimal.valueOf(100L);
    @Override
    public BigDecimal applyDiscount(BigDecimal amount, DiscountInfo discountInfo) {
        BigDecimal calculatedAmount = amount.multiply(
                discountInfo.discountValue().divide(HUNDRED, 2, RoundingMode.HALF_UP)
        );
        if (calculatedAmount.compareTo(discountInfo.maxDiscountValue()) > 0) {
            return amount.subtract(discountInfo.maxDiscountValue());
        }

        return amount.subtract(calculatedAmount);
    }
}
