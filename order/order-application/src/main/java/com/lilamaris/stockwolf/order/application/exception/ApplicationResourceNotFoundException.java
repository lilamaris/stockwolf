package com.lilamaris.stockwolf.order.application.exception;

public class ApplicationResourceNotFoundException extends ApplicationException {
    public ApplicationResourceNotFoundException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
