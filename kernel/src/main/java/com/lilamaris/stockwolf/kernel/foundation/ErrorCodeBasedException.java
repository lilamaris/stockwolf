package com.lilamaris.stockwolf.kernel.foundation;

import com.lilamaris.stockwolf.kernel.core.exception.ErrorCode;

public class ErrorCodeBasedException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorCodeBasedException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
