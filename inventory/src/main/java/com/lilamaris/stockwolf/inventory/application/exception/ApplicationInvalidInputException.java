package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationInvalidInputException extends ApplicationException {
    public ApplicationInvalidInputException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
