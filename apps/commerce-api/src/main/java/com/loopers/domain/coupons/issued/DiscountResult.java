package com.loopers.domain.coupons.issued;

import java.math.BigDecimal;

public record DiscountResult (
        BigDecimal finalPrice,
        BigDecimal discountAmount
){
}
