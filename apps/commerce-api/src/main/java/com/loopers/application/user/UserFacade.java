package com.loopers.application.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo find(String userId){
        return userService.find(userId)
                .map(UserInfo::from)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "이미 존재하는 User 입니다."));
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
