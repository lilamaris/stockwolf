package com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa;

import com.lilamaris.stockwolf.identity.application.port.out.AccountStore;
import com.lilamaris.stockwolf.identity.domain.Account;
import com.lilamaris.stockwolf.identity.domain.Provider;
import com.lilamaris.stockwolf.identity.infrastructure.persistence.jpa.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountStore {
    private final AccountRepository repository;

    @Override
    public boolean isExists(Provider provider, String providerId) {
        return repository.existsByProviderAndProviderId(provider, providerId);
    }

    @Override
    public Optional<Account> getAccount(Provider provider, String providerId) {
        return repository.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }
}
