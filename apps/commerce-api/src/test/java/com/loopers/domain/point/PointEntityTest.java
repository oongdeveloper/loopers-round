package com.loopers.domain.point;

import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointEntityTest {
    @DisplayName("포인트 충전 단위 테스트 ")
    @Nested
    class Charge {

        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @Test
        void failed_whenChargePointIsLessThanZero(){
            // arrange
            UserEntity user = new UserEntity(
                    "oong",
                    "오옹",
                    UserEntity.Gender.M,
                    "2025-07-01",
                    "oo@nn.gg"
            );
            // act
            // TODO. 이렇게 만들까? final 을 왜 추가하는지 기본적으로 고민.
            final CoreException exception = assertThrows(CoreException.class, () -> {
                user.charge(0L);
            });
            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            assertThat(exception.getCustomMessage()).isEqualTo("충전 금액은 0원 이상이어야 합니다.");
        }
    }
}
