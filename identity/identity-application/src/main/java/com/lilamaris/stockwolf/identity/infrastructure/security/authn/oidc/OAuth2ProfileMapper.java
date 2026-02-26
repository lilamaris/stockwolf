package com.lilamaris.stockwolf.identity.infrastructure.security.authn.oidc;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2ProfileMapper {
    boolean supports(String registrationId);

    NormalizedProfile map(OAuth2User oAuth2User, String registrationId);
}
