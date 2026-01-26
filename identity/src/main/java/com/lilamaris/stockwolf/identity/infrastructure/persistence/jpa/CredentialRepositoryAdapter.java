package com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.identity.application.port.out.CredentialStore;
import com.lilamaris.stockwolf.identity.domain.Credential;
import com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CredentialRepositoryAdapter implements CredentialStore {
    private final CredentialRepository repository;

    @Override
    public boolean isExists(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<Credential> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Credential save(Credential credential) {
        return repository.save(credential);
    }
}
