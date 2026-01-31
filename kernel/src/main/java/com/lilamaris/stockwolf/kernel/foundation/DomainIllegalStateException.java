package com.lilamaris.stockwolf.kernel.foundation;

import com.lilamaris.stockwolf.kernel.core.exception.ErrorCode;

public class DomainIllegalStateException extends ErrorCodeBasedException {
    public DomainIllegalStateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
