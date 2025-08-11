package com.loopers.domain.coupons.issued;

import java.util.Optional;

public interface UserCouponRepository {
    Optional<UserCoupon> findUserCouponForUpdate(Long userId, Long couponId);
    Optional<UserCoupon> findUserCoupon(Long userId, Long couponId);
}
