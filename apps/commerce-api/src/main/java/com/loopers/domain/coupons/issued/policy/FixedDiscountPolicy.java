package com.loopers.domain.coupons.issued.policy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedDiscountPolicy implements DiscountPolicy{
    @Override
    public BigDecimal applyDiscount(BigDecimal amount, DiscountInfo discountInfo) {
        if(amount.compareTo(discountInfo.discountValue()) < 0){
            return amount;
        }

        BigDecimal calculatedAmount = amount.subtract(discountInfo.discountValue());
        if (calculatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return calculatedAmount;
    }
}
