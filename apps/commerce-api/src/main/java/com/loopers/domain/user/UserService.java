package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserCommand command){
        try{
            userRepository.find(command.userId())
                    .ifPresent(e -> {
                        throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");
                    });

            return userRepository.save(User.from(command));
        } catch (DataIntegrityViolationException e){
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");
        }
    }

    public User find(String userId){
        return userRepository.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."));
    }

    public Long findUserPkId(String userId){
        return userRepository.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 User 입니다."))
                .getId();
    }
}
