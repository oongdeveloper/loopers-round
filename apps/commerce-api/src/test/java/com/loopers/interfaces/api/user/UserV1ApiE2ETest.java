package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ApiE2ETest {
    private final TestRestTemplate testRestTemplate;
    private final UserService userService;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2ETest(TestRestTemplate testRestTemplate, UserService userService, DatabaseCleanUp databaseCleanUp) {
        this.testRestTemplate = testRestTemplate;
        this.userService = userService;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Post{
        private final String END_POINT = "/api/v1/users";

        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserInfo_whenSuccessedSignUp(){
            UserV1Dto.SignUpRequest userRequest = new UserV1Dto.SignUpRequest(
                                                        "oong",
                                                        "옹재성",
                                                        UserV1Dto.Gender.M,
                                                    "2025-06-01",
                                                        "aa@bb.cc"
                                                );

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.POST, new HttpEntity<>(userRequest), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(userRequest.userId()),
                    () -> assertThat(response.getBody().data().name()).isEqualTo(userRequest.userName()),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(userRequest.gender()),
                    () -> assertThat(response.getBody().data().birth()).isEqualTo(userRequest.birth()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(userRequest.email())
            );
        }

        @DisplayName("회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void throwBadRequest_whenDoNotHaveGender(){
            final String END_POINT = "/api/v1/users";

            UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
                    "oong",
                    "옹재성",
                    null,
                    "2025-06-01",
                    "aa@bb.cc"
            );

            ParameterizedTypeReference<ApiResponse<?>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<?>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.POST, new HttpEntity<>(signUpRequest), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody().data()).isNull(),
                    () -> assertThat(response.getBody().errors()).isNotNull(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL)
            );
        }
    }

    @DisplayName("GET /api/v1/users/me")
    @Nested
    class Get{
        private final String END_POINT = "/api/v1/users/me";
        private final String ENROLLED_USER = "oong";

        @BeforeEach
        void setUp(){
            userService.save(UserCommand.of(
                    ENROLLED_USER,
                    "오옹",
                    User.Gender.M,
                    "2025-06-01",
                    "oong@oo.ng"
            ));
        }

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserInfo_whenSuccessedGetProfile(){
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", ENROLLED_USER);

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().data().name()).isEqualTo("오옹"),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(UserV1Dto.Gender.M)
            );
        }


        @DisplayName("존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다.")
        @Test
        void returnNOT_FOUND_whenFailedToGetProfile(){
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "TEST_USER");
            // act
            ParameterizedTypeReference<ApiResponse<?>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ApiResponse<?>> response =
                    testRestTemplate.exchange(END_POINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
            );
        }
    }

}
