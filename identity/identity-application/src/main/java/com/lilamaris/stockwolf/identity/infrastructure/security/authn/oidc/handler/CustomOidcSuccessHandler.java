package com.lilamaris.stockwolf.identity.infrastructure.security.authn.oidc.handler;

import com.lilamaris.stockwolf.identity.application.exception.IdentityApplicationException;
import com.lilamaris.stockwolf.identity.application.port.in.Authorizer;
import com.lilamaris.stockwolf.identity.application.port.in.TokenEntry;
import com.lilamaris.stockwolf.identity.application.port.in.TokenIssuer;
import com.lilamaris.stockwolf.identity.domain.Provider;
import com.lilamaris.stockwolf.identity.infrastructure.security.authn.oidc.NormalizedProfile;
import com.lilamaris.stockwolf.identity.infrastructure.security.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOidcSuccessHandler implements AuthenticationSuccessHandler {
    private final Authorizer authorizer;
    private final TokenIssuer issuer;
    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        TokenEntry tokenEntry = null;

        try {
            NormalizedProfile principal = (NormalizedProfile) authentication.getPrincipal();
            var provider = Provider.valueOf(principal.getProvider());
            var providerId = principal.getProviderId();
            var email = principal.getEmail();
            var displayName = principal.getDisplayName();

            var principalEntry = authorizer.signIn(provider, providerId, displayName);
            tokenEntry = issuer.issue(principalEntry.userId());
        } catch (IdentityApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("SignIn failed.", e);
        }

        writer.write(response, tokenEntry);
    }
}
