package com.lilamaris.stockwolf.identity.application.port.out;

import com.lilamaris.stockwolf.identity.domain.Account;
import com.lilamaris.stockwolf.identity.domain.Provider;

import java.util.Optional;

public interface AccountStore {
    boolean isExists(Provider provider, String providerId);

    Optional<Account> getAccount(Provider provider, String providerId);

    Account save(Account account);
}
