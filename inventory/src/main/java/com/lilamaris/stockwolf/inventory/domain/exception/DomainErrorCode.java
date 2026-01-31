package com.lilamaris.stockwolf.inventory.domain.exception;

import com.lilamaris.stockwolf.kernel.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    INVALID_RESERVE_QUANTITY("IV001", "재고량보다 더 많이 예약할 수 없습니다."),
    INVALID_RELEASE_QUANTITY("IV002", "예약 재고량보다 더 많이 해제할 수 없습니다."),
    INVALID_COMMIT_QUANTITY("IV003", "전체 예약 수량보다 더 많이 확정할 수 없습니다."),

    INVALID_RESERVATION_STATUS("RS001", "변경 가능한 상태가 아닙니다."),
    INVALID_QUANTITY("RS002", "수량은 0보다 커야합니다.");;

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
