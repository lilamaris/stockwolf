package com.lilamaris.stockwolf.order.application.exception;

public class ApplicationInvalidInputException extends ApplicationException {
    public ApplicationInvalidInputException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
