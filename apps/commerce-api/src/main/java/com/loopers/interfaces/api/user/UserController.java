package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class UserController implements UserV1ApiSpec{
    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping("/users")
    public ApiResponse<UserV1Dto.UserResponse> signUp(@Valid @RequestBody UserV1Dto.SignUpRequest request) {
        UserInfo userInfo = userFacade.signUp(request);
        return ApiResponse.success(UserV1Dto.UserResponse.from(userInfo));
    }

    @GetMapping("/users/me")
    public ApiResponse<UserV1Dto.UserResponse> get(@RequestHeader("X-USER-ID") String userId) {
        UserInfo userInfo = userFacade.get(userId);
        return ApiResponse.success(UserV1Dto.UserResponse.from(userInfo));
    }
}
