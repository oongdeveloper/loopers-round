package com.loopers.domain.user;

import com.loopers.application.user.UserInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRespository userRespository;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public UserEntity save(UserCommand command){
        userRespository.find(command.getUserId())
                .ifPresent(e -> {
                    throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");
                });

        // TODO. 변환 메소드 생성
        UserEntity newUser = new UserEntity(command.getUserId(),
                                            command.getName(),
                                            command.getGender(),
                                            command.getBirth(),
                                            command.getEmail());
        return userRespository.save(newUser);
    }

    // TODO. 수정 해야 함.
    // Facade 가 있어야 Optional 로 반환 가능.
    public Optional<UserEntity> find(String userId){
        return userRespository.find(userId);
    }
}
