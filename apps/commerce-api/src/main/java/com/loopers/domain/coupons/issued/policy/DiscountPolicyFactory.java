package com.loopers.domain.coupons.issued.policy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.loopers.domain.coupons.issued.policy.DiscountInfo.*;

@Component
public class DiscountPolicyFactory {

    private final Map<DiscountType, DiscountPolicy> policies;

    public DiscountPolicyFactory() {
        policies = new HashMap<>();
        this.policies.put(DiscountType.FIXED, new FixedDiscountPolicy());
        this.policies.put(DiscountType.RATE, new RateDiscountPolicy());
    }

    public DiscountPolicy getPolicy(DiscountType type){
        return Optional.ofNullable(policies.get(type))
                .orElseThrow(() -> new IllegalArgumentException(String.format("지원하지 않는 쿠폰 타입입니다. %s", type)));
    }
}
