package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.provider;

import com.lilamaris.stockwolf.identity.application.exception.IdentityApplicationException;
import com.lilamaris.stockwolf.identity.application.port.in.Authorizer;
import com.lilamaris.stockwolf.identity.application.port.in.TokenIssuer;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.AuthenticatedToken;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.CredentialSignInAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CredentialSignInProvider implements AuthenticationProvider {
    private final Authorizer authorizer;
    private final TokenIssuer issuer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var auth = (CredentialSignInAuthentication) authentication;
            var email = auth.getEmail();
            var rawPassword = auth.getRawPassword();

            var principal = authorizer.signIn(email, rawPassword);
            var token = issuer.issue(principal.userId());

            return new AuthenticatedToken(
                    token.accessToken(),
                    token.refreshToken()
            );
        } catch (IdentityApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("SignIn failed.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CredentialSignInAuthentication.class.isAssignableFrom(authentication);
    }
}
