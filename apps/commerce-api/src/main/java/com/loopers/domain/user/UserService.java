package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRespository userRespository;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public UserEntity save(UserCommand command){
        UserEntity user = userRespository.find(command.getUserId());
        if (user != null) throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");

        UserEntity newUser = new UserEntity(command.getUserId(),
                                            command.getName(),
                                            command.getGender(),
                                            command.getBirth(),
                                            command.getEmail());
        return userRespository.save(newUser);
    }
}
