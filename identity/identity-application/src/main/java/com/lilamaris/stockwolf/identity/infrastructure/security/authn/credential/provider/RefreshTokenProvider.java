package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.provider;

import com.lilamaris.stockwolf.identity.application.exception.IdentityApplicationException;
import com.lilamaris.stockwolf.identity.application.port.in.Authorizer;
import com.lilamaris.stockwolf.identity.application.port.in.TokenIssuer;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.AuthenticatedToken;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token.RefreshTokenAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenProvider implements AuthenticationProvider {
    private final Authorizer authorizer;
    private final TokenIssuer issuer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var auth = (RefreshTokenAuthentication) authentication;
            var refreshToken = auth.getRefreshToken();

            var token = authorizer.reissue(refreshToken);

            return new AuthenticatedToken(
                    token.accessToken(),
                    token.refreshToken()
            );
        } catch (IdentityApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.info("Refresh Error: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("Refresh failed.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
