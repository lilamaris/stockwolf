package com.lilamaris.stockwolf.identity.application.port.in;

import com.lilamaris.stockwolf.identity.domain.Provider;

public interface Authorizer {
    PrincipalEntry signIn(String email, String rawPassword);

    PrincipalEntry signIn(Provider provider, String providerId, String displayName);

    TokenEntry reissue(String refreshToken);
}
