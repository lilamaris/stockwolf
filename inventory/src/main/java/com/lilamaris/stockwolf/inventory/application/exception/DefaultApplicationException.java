package com.lilamaris.stockwolf.inventory.application.exception;

import lombok.Getter;

@Getter
public class DefaultApplicationException extends RuntimeException {
    private final ApplicationErrorCode errorCode;

    public DefaultApplicationException(ApplicationErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
