package com.loopers.infrastructure.coupons.issued;

import com.loopers.domain.coupons.issued.UserCoupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT uc 
            FROM UserCoupon uc
            WHERE uc.userId = :userId
              AND uc.couponId = :couponId
              AND uc.expiresAt > CURRENT_TIMESTAMP
            """)
    Optional<UserCoupon> findUserCouponForUpdate(@Param("userId") Long userId, @Param("couponId") Long couponId);

    @Query(value = """
            SELECT uc 
            FROM UserCoupon uc
            WHERE uc.userId = :userId
              AND uc.id = :couponId
              AND uc.expiresAt > CURRENT_TIMESTAMP
            """)
    Optional<UserCoupon> findUserCoupon(@Param("userId") Long userId, @Param("couponId") Long couponId);
}
