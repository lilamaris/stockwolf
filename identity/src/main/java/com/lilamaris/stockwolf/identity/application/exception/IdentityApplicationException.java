package com.lilamaris.stockwolf.identity.application.exception;

import lombok.Getter;

@Getter
public class IdentityApplicationException extends RuntimeException {
    private final IdentityErrorCode errorCode;

    public IdentityApplicationException(IdentityErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
