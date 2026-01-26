package com.lilamaris.stockwolf.identity.application.service;

import com.lilamaris.stockwolf.identity.application.exception.IdentityErrorCode;
import com.lilamaris.stockwolf.identity.application.exception.IdentityIllegalStateException;
import com.lilamaris.stockwolf.identity.application.exception.IdentityNotFoundException;
import com.lilamaris.stockwolf.identity.application.port.in.*;
import com.lilamaris.stockwolf.identity.application.port.out.AccountStore;
import com.lilamaris.stockwolf.identity.application.port.out.CredentialStore;
import com.lilamaris.stockwolf.identity.application.port.out.RefreshTokenStore;
import com.lilamaris.stockwolf.identity.application.port.out.UserStore;
import com.lilamaris.stockwolf.identity.domain.Account;
import com.lilamaris.stockwolf.identity.domain.Credential;
import com.lilamaris.stockwolf.identity.domain.Provider;
import com.lilamaris.stockwolf.identity.domain.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentityService implements
        Authorizer,
        UserRegister,
        TokenIssuer {
    private final UserStore userStore;
    private final RefreshTokenStore refreshTokenStore;
    private final AccountStore accountStore;
    private final CredentialStore credentialStore;

    private final JwtEncoder jwtEncoder;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    private String DUMMY_HASH;

    @PostConstruct
    private void init() {
        this.DUMMY_HASH = passwordEncoder.encode(createOpaqueString());
    }

    @Override
    public PrincipalEntry signIn(String email, String rawPassword) {
        var credential = credentialStore.getByEmail(email);

        boolean isUserAbsent = credential.isEmpty();
        if (isUserAbsent) {
            passwordEncoder.matches(rawPassword, DUMMY_HASH);
        }

        return credential
                .filter(c -> passwordEncoder.matches(rawPassword, c.getPasswordHash()))
                .filter(c -> userStore.isExists(c.getUserId()))
                .map(c -> new PrincipalEntry(c.getUserId()))
                .orElseThrow(() -> new IdentityNotFoundException(IdentityErrorCode.AUTHENTICATION_FAILED));
    }

    @Override
    public PrincipalEntry signIn(Provider provider, String providerId, String displayName) {
        return accountStore.getAccount(provider, providerId)
                .filter(a -> userStore.isExists(a.getUserId()))
                .map(a -> new PrincipalEntry(a.getUserId()))
                .orElseGet(() -> {
                    var user = User.create(displayName);
                    user = userStore.save(user);
                    var account = Account.create(provider, providerId, user.getId());
                    account = accountStore.save(account);
                    return new PrincipalEntry(user.getId());
                });
    }

    @Override
    public PrincipalEntry register(String email, String name, String rawPassword) {
        if (credentialStore.isExists(email)) {
            throw new IdentityIllegalStateException(IdentityErrorCode.EMAIL_ALREADY_IN_USE);
        }
        var passwordHash = passwordEncoder.encode(rawPassword);
        var user = User.create(name);
        user = userStore.save(user);
        var credential = Credential.create(email, passwordHash, user.getId());
        credential = credentialStore.save(credential);

        return new PrincipalEntry(user.getId());
    }

    @Override
    public TokenEntry reissue(String refreshToken) {
        return null;
    }

    @Override
    public TokenEntry issue(UUID userId) {
        var subject = userId.toString();
        var accessToken = createAccessToken(subject);

        var refreshTokenKey = createOpaqueString();
        refreshTokenStore.save(refreshTokenKey, subject, refreshTokenExpiration);

        return new TokenEntry(accessToken, refreshTokenKey);
    }

    private String createAccessToken(String subject) {
        var now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("identity-service")
                .subject(subject)
                .audience(List.of("api"))
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpiration))
                .claim("scope", "order:read orders:write")
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }

    private String createOpaqueString() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
