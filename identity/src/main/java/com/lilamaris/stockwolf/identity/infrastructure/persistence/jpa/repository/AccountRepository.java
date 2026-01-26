package com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa.repository;

import com.lilamaris.stockwolf.identity.domain.Account;
import com.lilamaris.stockwolf.identity.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    Optional<Account> findByProviderAndProviderId(Provider provider, String providerId);
}
