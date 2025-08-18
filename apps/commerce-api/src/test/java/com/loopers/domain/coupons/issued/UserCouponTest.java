package com.loopers.domain.coupons.issued;

import com.loopers.domain.coupons.master.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {
    private final ZonedDateTime now = ZonedDateTime.now();

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        private UserCoupon createUserCoupon(Coupon.CouponType type) {
            return UserCoupon.of(
                    1L, 100L, now.plusDays(7),
                    "테스트 쿠폰", type,
                    "ALL", null,
                    BigDecimal.valueOf(5000L),
                    (type == Coupon.CouponType.RATE) ? BigDecimal.valueOf(10L) : BigDecimal.valueOf(10000L),
                    BigDecimal.valueOf(3000L)
            );
        }

        @Test
        @DisplayName("정액 쿠폰이 정상적으로 생성된다")
        void create_fixed_coupon_success() {
            Coupon.CouponType type = Coupon.CouponType.FIXED;

            UserCoupon userCoupon = createUserCoupon(type);

            assertNotNull(userCoupon);
            assertFalse(userCoupon.isUsed());
            assertNotNull(userCoupon.getIssuedAt());
            assertEquals(type, userCoupon.getType());
        }

        @Test
        @DisplayName("정률 쿠폰이 정상적으로 생성된다")
        void create_rate_coupon_success() {
            Coupon.CouponType type = Coupon.CouponType.RATE;

            UserCoupon userCoupon = createUserCoupon(type);

            // then
            assertNotNull(userCoupon);
            assertFalse(userCoupon.isUsed());
            assertNotNull(userCoupon.getIssuedAt());
            assertEquals(type, userCoupon.getType());
            assertTrue(userCoupon.getDiscountValue().compareTo(new BigDecimal("100")) < 0);
        }

        @ParameterizedTest(name = "{0} 필드가 null일 때 IllegalArgumentException 발생")
        @MethodSource("provideNullArguments")
        @DisplayName("필수 필드가 null이면 예외가 발생한다")
        void create_with_null_field_throws_exception(Long userId, Long couponId, ZonedDateTime expiresAt, Coupon.CouponType type, BigDecimal discountValue) {
            assertThrows(NullPointerException.class, () -> UserCoupon.of(
                    userId, couponId, expiresAt,
                    "테스트 쿠폰", type,
                    "ALL", null,
                    BigDecimal.valueOf(5000L),
                    discountValue,
                    BigDecimal.valueOf(3000L)
            ));
        }

        static Stream<Arguments> provideNullArguments() {
            return Stream.of(
                    Arguments.of(null, 100L, ZonedDateTime.now().plusDays(7), Coupon.CouponType.FIXED, new BigDecimal("10000")),
                    Arguments.of(1L, null, ZonedDateTime.now().plusDays(7), Coupon.CouponType.FIXED, new BigDecimal("10000")),
                    Arguments.of(1L, 100L, null, Coupon.CouponType.FIXED, new BigDecimal("10000")),
                    Arguments.of(1L, 100L, ZonedDateTime.now().plusDays(7), null, new BigDecimal("10000")),
                    Arguments.of(1L, 100L, ZonedDateTime.now().plusDays(7), Coupon.CouponType.FIXED, null)
            );
        }

        @Test
        @DisplayName("정률 할인율이 100을 초과하면 예외가 발생한다")
        void create_rate_coupon_over_100_throws_exception() {
            BigDecimal invalidRate = new BigDecimal("100.01");
            assertThrows(IllegalArgumentException.class, () -> UserCoupon.of(
                    1L, 100L, now.plusDays(7),
                    "테스트 쿠폰", Coupon.CouponType.RATE,
                    "ALL", null,
                    BigDecimal.valueOf(50000L),
                    invalidRate,
                    BigDecimal.valueOf(30000L)
            ));
        }

        @ParameterizedTest(name = "{0}이 음수이면 예외가 발생한다")
        @MethodSource("provideNegativeMoneyArguments")
        @DisplayName("금액 관련 필드가 음수이면 예외가 발생한다")
        void create_with_negative_money_throws_exception(BigDecimal maxDiscountValue, BigDecimal minOrderAmount) {
            assertThrows(IllegalArgumentException.class, () -> UserCoupon.of(
                    1L, 100L, now.plusDays(7),
                    "테스트 쿠폰", Coupon.CouponType.FIXED,
                    "ALL", null,
                    maxDiscountValue,
                    BigDecimal.valueOf(10000L),
                    minOrderAmount
            ));
        }

        static Stream<Arguments> provideNegativeMoneyArguments() {
            return Stream.of(
                    Arguments.of(BigDecimal.valueOf(-1L), BigDecimal.valueOf(30000L)), // maxDiscountValue 음수
                    Arguments.of(BigDecimal.valueOf(50000L), BigDecimal.valueOf(-1L))   // minOrderAmount 음수
            );
        }


        @ParameterizedTest(name = "{0}이 100억을 초과하면 예외가 발생한다")
        @MethodSource("provideExceededMoneyArguments")
        @DisplayName("금액 관련 필드가 100억을 초과하면 예외가 발생한다")
        void create_with_exceeded_money_throws_exception(BigDecimal maxDiscountValue, BigDecimal minOrderAmount) {
            assertThrows(IllegalArgumentException.class, () -> UserCoupon.of(
                    1L, 100L, now.plusDays(7),
                    "테스트 쿠폰", Coupon.CouponType.FIXED,
                    "ALL", null,
                    maxDiscountValue,
                    BigDecimal.valueOf(10000L),
                    minOrderAmount
            ));
        }

        static Stream<Arguments> provideExceededMoneyArguments() {
            BigDecimal exceeded = new BigDecimal(10_000_000_001L);
            return Stream.of(
                    Arguments.of(exceeded, BigDecimal.valueOf(3000L)),
                    Arguments.of(BigDecimal.valueOf(3000L), exceeded)
            );
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 테스트")
    class BusinessLogicTest {
        private UserCoupon createUserCoupon(Coupon.CouponType type) {
            return UserCoupon.of(
                    1L, 100L, now.plusDays(7),
                    "테스트 쿠폰", type,
                    "ALL", null,
                    BigDecimal.valueOf(5000L),
                    (type == Coupon.CouponType.RATE) ? BigDecimal.valueOf(10L) : BigDecimal.valueOf(10000L),
                    BigDecimal.valueOf(3000L)
            );
        }

        @Test
        @DisplayName("쿠폰 사용 메소드가 정상 동작한다")
        void use_coupon_success() {
            UserCoupon userCoupon = createUserCoupon(Coupon.CouponType.FIXED);

            userCoupon.use();

            assertTrue(userCoupon.isUsed());
            assertNotNull(userCoupon.getUsedAt());
        }

        @Test
        @DisplayName("이미 사용된 쿠폰을 다시 사용하려 하면 예외가 발생한다")
        void use_already_used_coupon_throws_exception() {
            UserCoupon userCoupon = createUserCoupon(Coupon.CouponType.FIXED);
            userCoupon.use();

            assertThrows(IllegalStateException.class, userCoupon::use);
        }
    }

}
