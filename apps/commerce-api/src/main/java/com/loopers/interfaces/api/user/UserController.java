package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class UserController implements UserV1ApiSpec{
    private final UserService userService;
    private final UserFacade userFacade;

    public UserController(UserService userService, UserFacade userFacade) {
        this.userService = userService;
        this.userFacade = userFacade;
    }

    @PostMapping("/users")
    public ApiResponse<UserV1Dto.UserResponse> signUp(@Valid @RequestBody UserV1Dto.SignUpRequest request) {
//        userService.signUp(userInfo);

//        if (request.gender() == null) {
//            throw new CoreException(ErrorType.BAD_REQUEST,"성별은 빈 값일 수 없습니다.");
//        }

        UserV1Dto.UserResponse response = new UserV1Dto.UserResponse(
                "oong",
                "옹재성",
                UserV1Dto.Gender.M,
                "2025-06-01",
                "aa@bb.cc"
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/users/me")
    public ApiResponse<UserV1Dto.UserResponse> signUp(@RequestHeader("X-USER-ID") String userId) {
        UserInfo userInfo = userFacade.find(userId);
        System.out.printf("userInfo == " + userInfo);
        UserV1Dto.UserResponse response =  UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }
}
