package com.loopers.domain.point;


import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PointServiceIntegrationTest {
    private final PointService pointService;
    private final DatabaseCleanUp databaseCleanUp;
    private final UserService userService;

    @Autowired
    public PointServiceIntegrationTest(PointService pointService, DatabaseCleanUp databaseCleanUp, UserService userService) {
        this.pointService = pointService;
        this.databaseCleanUp = databaseCleanUp;
        this.userService = userService;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트 조회")
    @Nested
    class Point{
        final String ENROLLED_USER = "oong";

        @BeforeEach
        void setUp(){
            userService.save(UserCommand.of(
                    ENROLLED_USER,
                    "오옹",
                    UserEntity.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            ));
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnUserPoint_whenUserExist(){
            // arrange
            // act
            Optional<UserEntity> user = pointService.find(ENROLLED_USER);

            // assert
            assertTrue(user.isPresent());
            assertThat(user.get().getUserId()).isEqualTo(ENROLLED_USER);
            assertThat(user.get().getPoint()).isEqualTo(0L);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenUserIsNotExist(){
            // arrange
            // act
            Optional<UserEntity> user = pointService.find("TEST_USER");
            // assert
            assertTrue(user.isEmpty());
        }
    }

}
