package com.lilamaris.stockwolf.order.application.exception;

public class ApplicationInternalException extends ApplicationException {
    public ApplicationInternalException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
