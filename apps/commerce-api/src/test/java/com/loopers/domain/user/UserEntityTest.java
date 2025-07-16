package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserEntityTest {

    @DisplayName("회원 가입 단위 테스트 ")
    @Nested
    class signUp {
        @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면 User 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "옹",
                "aaa__aa",
                "aaaaa000000",
                ""
        })
        void throwBadRequestException_whenIdDoesNotMatch(String userId) {
            // arrange
            // act
            // assert
            assertThatThrownBy(() -> {
                new UserEntity(
                        userId,
                        "오옹",
                        UserEntity.Gender.M,
                        "2025-07-01",
                        "oo@nn.gg"
                );
            }).isInstanceOf(CoreException.class).hasMessage("ID 는 1자 이상 10자 이내의 영문과 숫자로 이루어져야 합니다.");

            // TODO. 이렇게 만들까? final 을 왜 추가하는지 기본적으로 고민.
            final CoreException exception = assertThrows(CoreException.class, () -> {
                new UserEntity(
                        userId,
                        "오옹",
                        UserEntity.Gender.M,
                        "2025-07-01",
                        "oo@nn.gg"
                );
            });
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면 User 객체 생성에 실패한다.")
        @Test
        void throwBadRequestException_whenEmailDoesNotMatch() {
            // arrange
            // act
            // assert
            assertThatThrownBy(() -> {
                new UserEntity(
                        "oong",
                        "오옹",
                        UserEntity.Gender.M,
                        "2025-07-01",
                        "oo@nn@gg"
                );
            }).isInstanceOf(CoreException.class).hasMessage("이메일 형식이 올바르지 않습니다. ex) aa@bb.cc");
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면 User 객체 생성에 실패한다.")
        @Test
        void throwBadRequestException_whenBirthDoesNotMatch() {
            // arrange
            // act
            // assert
            assertThatThrownBy(() -> {
                new UserEntity(
                        "oong",
                        "오옹",
                        UserEntity.Gender.M,
                        "20250701",
                        "oo@nn.gg"
                );
            }).isInstanceOf(CoreException.class).hasMessage("생년월일 형식이 맞지 않습니다. f) yyyy-MM-dd");
        }
    }
}
