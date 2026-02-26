package com.lilamaris.stockwolf.identity.application.port.in;

public record TokenEntry(
        String accessToken,
        String refreshToken
) {
}
