package com.lilamaris.stockwolf.identity.application.port.out;

import com.lilamaris.stockwolf.identity.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserStore {
    boolean isExists(UUID userId);

    Optional<User> getUser(UUID userId);

    User save(User user);
}
