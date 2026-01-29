package com.lilamaris.stockwolf.inventory.domain.exception;

import lombok.Getter;

@Getter
public class DefaultDomainException extends RuntimeException {
    private final DomainErrorCode errorCode;

    public DefaultDomainException(DomainErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
