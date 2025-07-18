package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity save(UserCommand command){
        try{
            userRepository.find(command.getUserId())
                    .ifPresent(e -> {
                        throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");
                    });

            return userRepository.save(UserEntity.from(command));
        } catch (DataIntegrityViolationException e){
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 User 입니다.");
        }
    }

    public Optional<UserEntity> find(String userId){
        return userRepository.find(userId);
    }
}
