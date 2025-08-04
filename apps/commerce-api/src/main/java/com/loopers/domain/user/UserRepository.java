package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> find(String findId);
    User save(User user);
}
