package com.loopers.application.user;

import com.loopers.application.example.ExampleInfo;
import com.loopers.domain.example.ExampleModel;
import com.loopers.domain.example.ExampleService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo find(String userId){
        return UserInfo.of(
                userService.find(userId).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "이미 존재하는 User 입니다."))
        );
    }

    // TODO. SAVE 만들어야 됨.
}
