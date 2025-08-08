package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo get(String userId){
        return UserInfo.from(userService.find(userId));
    }

    public UserInfo signUp(UserV1Dto.SignUpRequest request){
        return UserInfo.from(userService.save(requestToCommand(request)));
    }

    private UserCommand requestToCommand(UserV1Dto.SignUpRequest request) {
        return UserCommand.of(
                request.userId(),
                request.userName(),
                request.gender(),
                request.birth(),
                request.email());
    }
}
