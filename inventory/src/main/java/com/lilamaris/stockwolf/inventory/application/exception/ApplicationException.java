package com.lilamaris.stockwolf.inventory.application.exception;

import com.lilamaris.stockwolf.kernel.foundation.ErrorCodeBasedException;
import lombok.Getter;

@Getter
public class ApplicationException extends ErrorCodeBasedException {
    private final ApplicationErrorCode errorCode;

    public ApplicationException(ApplicationErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
