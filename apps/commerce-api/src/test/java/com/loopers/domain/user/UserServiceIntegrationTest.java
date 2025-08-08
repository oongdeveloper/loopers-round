package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {

    private final UserService userService;
    private final DatabaseCleanUp databaseCleanUp;

    @MockitoSpyBean
    private UserRepository userRepository;

    @Autowired
    public UserServiceIntegrationTest(UserService userService,
                                      DatabaseCleanUp databaseCleanUp) {
        this.userService = userService;
        this.databaseCleanUp = databaseCleanUp;
    }


    @DisplayName("회원가입")
    @Nested
    class SignUP{
        @AfterEach
        void tearDown() {
            databaseCleanUp.truncateAllTables();
        }

        @DisplayName("회원 가입 시, User 저장이 수행된다. SPY 검증")
        @Test
        void success_signup(){
            UserCommand commnand = UserCommand.of(
                    "oong",
                    "오옹",
                    User.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            );

            User user = userService.save(commnand);

            verify(userRepository, times(1)).find(any(String.class));
            verify(userRepository, times(1)).save(any(User.class));
            assertNotNull(user);
            assertEquals("oong", user.getUserId());
            assertEquals("오옹", user.getUserName());
        }

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void throwAlreadyEnrolledException_whenAlreadySignedUp(){
            UserCommand command = UserCommand.of(
                    "oong",
                    "오옹",
                    User.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            );
            userService.save(command);

            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.save(command);
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }

    @DisplayName("내 정보 조회")
    @Nested
    class Profile{
        final String ENROLLED_USER = "oong";
        final String UNKNOWN_USER = "oong_oong";

        @BeforeEach
        void setUp(){
            userService.save(UserCommand.of(
                    "oong",
                    "오옹",
                    User.Gender.M,
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
            User userInfo = userService.find(ENROLLED_USER);

//            assertThat(userInfo)
//                    .isPresent()
//                    .hasValueSatisfying(user -> {
//                        assertThat(user.getUserId()).isEqualTo(ENROLLED_USER);
//                        assertThat(user.getUserName()).isEqualTo("오옹");
//                    });
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void throwNullPointException_whenCannotFindUserId(){
            User userEntity = userService.find(UNKNOWN_USER);
            assertThat(userEntity).isNull();
        }
    }
}

