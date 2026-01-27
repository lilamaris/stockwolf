package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.provider;

import com.lilamaris.stockwolf.identity.application.exception.IdentityApplicationException;
import com.lilamaris.stockwolf.identity.application.port.in.TokenIssuer;
import com.lilamaris.stockwolf.identity.application.port.in.UserRegister;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.AuthenticatedToken;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.CredentialRegisterAuthentication;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialRegisterProvider implements AuthenticationProvider {
    private final UserRegister userRegister;
    private final TokenIssuer issuer;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        try {
            var auth = (CredentialRegisterAuthentication) authentication;
            var email = auth.getEmail();
            var rawPassword = auth.getRawPassword();
            var displayName = auth.getDisplayName();

            var principal = userRegister.register(email, displayName, rawPassword);
            var token = issuer.issue(principal.userId());

            return new AuthenticatedToken(
                    token.accessToken(),
                    token.refreshToken()
            );
        } catch (IdentityApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Register failed.", e);
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return CredentialRegisterAuthentication.class.isAssignableFrom(authentication);
    }
}