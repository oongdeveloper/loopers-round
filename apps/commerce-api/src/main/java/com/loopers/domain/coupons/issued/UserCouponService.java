package com.loopers.domain.coupons.issued;

import com.loopers.domain.coupons.issued.policy.CouponCalculator;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserCouponService {
    private final CouponCalculator calculator;
    private final UserCouponRepository userCouponRepository;

    public UserCouponService(CouponCalculator calculator, UserCouponRepository userCouponRepository) {
        this.calculator = calculator;
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional
    public BigDecimal applyCoupon(UserCouponCommand.Apply command) {
//        UserCoupon coupon = userCouponRepository.findUserCouponForUpdate(command.userId(), command.couponId())
        UserCoupon coupon = userCouponRepository.findUserCoupon(command.userId(), command.couponId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,"사용자에게 존재하지 않는 쿠폰입니다. " + command.couponId()));
        coupon.use();
        return calculator.applyCoupon(coupon, command.amount());
    }
}
