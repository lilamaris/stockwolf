package com.lilamaris.stockwolf.identity.application.port.out;

import com.lilamaris.stockwolf.identity.domain.Credential;

import java.util.Optional;

public interface CredentialStore {
    boolean isExists(String email);

    Optional<Credential> getByEmail(String email);

    Credential save(Credential credential);
}
