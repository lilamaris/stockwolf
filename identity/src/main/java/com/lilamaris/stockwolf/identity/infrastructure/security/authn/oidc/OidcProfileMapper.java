package com.lilamaris.stockwolf.identity.infrastructure.security.authn.oidc;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OidcProfileMapper {
    boolean supports(String registrationId);

    NormalizedProfile map(OidcUser oidcUser, String registrationId);
}
