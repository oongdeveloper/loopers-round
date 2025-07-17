package com.loopers.interfaces.api.point;


import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {
    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final UserService userService;

    @Autowired
    public PointV1ApiE2ETest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp, UserService userService) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.userService = userService;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/points")
    @Nested
    class Get{
        final String END_POINT = "/api/v1/points";
        final String ENROLLED_USER = "oong";

        // TODO. Stub 으로 별도 처리 가능?
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

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnUserPoint_whenSuccessFindPoint(){
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", ENROLLED_USER);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserPointResponse>> responseType = new ParameterizedTypeReference<>(){};
            ResponseEntity<ApiResponse<UserV1Dto.UserPointResponse>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(ENROLLED_USER)
            );
        }

        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        @Test
        void returnBadRequest_whenHeaderUserIdMissing(){
            // arrange
            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserPointResponse>> responseType = new ParameterizedTypeReference<>(){};
            ResponseEntity<ApiResponse<UserV1Dto.UserPointResponse>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }

    }


}
