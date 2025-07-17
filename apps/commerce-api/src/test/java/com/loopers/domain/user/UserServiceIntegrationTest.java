package com.loopers.domain.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @DisplayName("회원가입")
    @Nested
    class signUP{
        @AfterEach
        void tearDown() {
            databaseCleanUp.truncateAllTables();
        }

        // TODO. SPY 로 바꿔야 됨.
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

    @DisplayName("내 정보 조회")
    @Nested
    class profile{
        @BeforeEach
        void init(){
            userService.save(UserCommand.of(
                    "oong",
                    "오옹",
                    UserEntity.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            ));
        }

        @AfterEach
        void tearDown() {
            databaseCleanUp.truncateAllTables();
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void success_whenFindExistUserId(){
            // arrange
            String userId = "oong";

            // act
            UserInfo userInfo = userFacade.find(userId);

            // assert
            assertAll(
                    () -> assertThat(userInfo.userId()).isEqualTo(userId),
                    () -> assertThat(userInfo.userName()).isEqualTo("오옹"),
                    () -> assertThat(userInfo.gender()).isEqualTo(UserEntity.Gender.M)
            );
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void throwNullPointException_whenCannotFindUserId(){
            // arrange
            String userId = "oong";
            // act
            Optional<UserEntity> userEntity = userService.find(userId);
            // assert
            assertTrue(IsOptionalNull(userEntity));
        }

        private boolean IsOptionalNull(Optional<?> optionalValue) {
            return optionalValue.isPresent() ? true : false;
        }
    }
}

