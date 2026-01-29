package com.lilamaris.stockwolf.inventory.domain.exception;

public class DomainIllegalStateException extends DefaultDomainException {
    public DomainIllegalStateException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}
