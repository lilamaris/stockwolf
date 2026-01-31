package com.lilamaris.stockwolf.inventory.application.exception;

import com.lilamaris.stockwolf.kernel.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {
    INVENTORY_NOT_FOUND("AR001", "제품을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND("AR002", "예약을 찾을 수 없습니다."),
    ;
    private final String code;
    private final String message;


    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
