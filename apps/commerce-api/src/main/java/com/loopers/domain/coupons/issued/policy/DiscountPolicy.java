package com.loopers.domain.coupons.issued.policy;

import java.math.BigDecimal;

public interface DiscountPolicy {
    BigDecimal applyDiscount(BigDecimal amount, DiscountInfo discountInfo);
}
