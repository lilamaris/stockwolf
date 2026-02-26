package com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.identity.application.port.out.UserStore;
import com.lilamaris.stockwolf.identity.domain.User;
import com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserStore {
    private final UserRepository repository;

    @Override
    public boolean isExists(UUID userId) {
        return repository.existsById(userId);
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return repository.findById(userId);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }
}
