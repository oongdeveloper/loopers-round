package com.loopers.infrastructure.coupons.issued;

import com.loopers.domain.coupons.issued.UserCoupon;
import com.loopers.domain.coupons.issued.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final UserCouponJpaRepository jpaRepository;

    public UserCouponRepositoryImpl(UserCouponJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<UserCoupon> findUserCouponForUpdate(Long userId, Long couponId) {
        return jpaRepository.findUserCouponForUpdate(userId, couponId);
    }

    @Override
    public Optional<UserCoupon> findUserCoupon(Long userId, Long couponId) {
        return jpaRepository.findUserCoupon(userId, couponId);
    }
}
