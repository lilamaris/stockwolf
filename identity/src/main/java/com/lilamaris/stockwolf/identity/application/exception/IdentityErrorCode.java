package com.lilamaris.stockwolf.identity.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdentityErrorCode {
    AUTHENTICATION_FAILED("ID001", "인증 실패"),
    EMAIL_ALREADY_IN_USE("ID002", "이미 사용중인 이메일입니다."),

    TOKEN_INVALID("ID05", "토큰이 유효하지 않습니다.");

    private final String code;
    private final String message;
}
