package com.loopers.domain.point;


import com.loopers.application.point.PointFacade;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PointServiceIntegrationTest {
//    private final PointFacade pointFacade;
    private final PointService pointService;
    private final DatabaseCleanUp databaseCleanUp;
    private final UserService userService;

    @Autowired
    public PointServiceIntegrationTest(PointFacade pointFacade, PointService pointService, DatabaseCleanUp databaseCleanUp, UserService userService) {
//        this.pointFacade = pointFacade;
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
        final String UNKNOWN_USER = "UNKNOWN_USER";

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
            Optional<UserEntity> user = pointService.find(ENROLLED_USER);

            assertTrue(user.isPresent());
            assertThat(user.get().getUserId()).isEqualTo(ENROLLED_USER);
            assertThat(user.get().getPoint()).isEqualTo(0L);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenUserIsNotExist(){
            Optional<UserEntity> user = pointService.find(UNKNOWN_USER);

            assertTrue(user.isEmpty());
        }
    }

    @DisplayName("포인트 충전")
    @Nested
    class Charge{
        final String END_POINT = "/api/v1/points/charge";
        final String ENROLLED_USER = "oong";
        final String UNKNOWN_USER = "UNKNOWN_USER";

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

        @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
        @Test
        void failed_whenChargeWithUnknownUserId(){
            CoreException exception = assertThrows(CoreException.class, () -> {
                pointService.charge(ChargePointCommand.of(UNKNOWN_USER, 1000L));
            });

            Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("충전이 완료된 경우, JPA 가 저장을 제대로 하는지 확인.")
        @Test
        void compare_successCharge_findOnDB(){
            long chargedPoint = pointService.charge(ChargePointCommand.of(ENROLLED_USER, 1000L));
            Optional<UserEntity> userAfterCharge = pointService.find(ENROLLED_USER);

            assertThat(chargedPoint).isEqualTo(userAfterCharge.get().getPoint());
        }
    }

}
