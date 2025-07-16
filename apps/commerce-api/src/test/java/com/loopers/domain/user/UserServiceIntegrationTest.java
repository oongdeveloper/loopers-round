package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
    private UserService userService;

    @Autowired
    public UserServiceIntegrationTest(UserService userService) {
        this.userService = userService;
    }

    @DisplayName("회원가입")
    @Nested
    class signUP{

        @DisplayName("회원 가입 시, User 저장이 수행된다. SPY 검증")
        @Test
        void success_signup(){
            // arrange
            UserCommand commnand = UserCommand.of(
                    "oong",
                    "오옹",
                    UserEntity.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            );
            // act
            UserEntity user = userService.save(commnand);

            // assert
            assertAll(
                    () -> assertThat(user.getUserId()).isEqualTo(commnand.getUserId()),
                    () -> assertThat(user.getName()).isEqualTo(commnand.getName()),
                    () -> assertThat(user.getBirth()).isEqualTo(commnand.getBirth()),
                    () -> assertThat(user.getGender()).isEqualTo(commnand.getGender()),
                    () -> assertThat(user.getEmail()).isEqualTo(commnand.getEmail())
            );
        }

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void throwAlreadyEnroledException_whenAlreadySignedUp(){
            // arrange
            UserCommand command = UserCommand.of(
                    "oong",
                    "오옹",
                    UserEntity.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            );
            userService.save(command);
            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.save(command);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }
}

