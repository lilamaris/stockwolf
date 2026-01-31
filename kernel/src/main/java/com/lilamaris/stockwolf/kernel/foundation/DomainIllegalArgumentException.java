package com.lilamaris.stockwolf.kernel.foundation;

import com.lilamaris.stockwolf.kernel.core.exception.ErrorCode;

public class DomainIllegalArgumentException extends ErrorCodeBasedException {
    public DomainIllegalArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
