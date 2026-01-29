package com.lilamaris.stockwolf.inventory.domain.exception;

import lombok.Getter;

@Getter
public class DefaultDomainException extends RuntimeException {
    private final ErrorCode errorCode;

    public DefaultDomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
