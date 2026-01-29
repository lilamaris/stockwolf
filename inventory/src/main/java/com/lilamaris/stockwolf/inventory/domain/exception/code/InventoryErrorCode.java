package com.lilamaris.stockwolf.inventory.domain.exception.code;

import com.lilamaris.stockwolf.inventory.domain.exception.DomainErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryErrorCode implements DomainErrorCode {
    INVALID_RESERVE_QUANTITY("IV001", "재고량보다 더 많이 예약할 수 없습니다."),
    INVALID_RELEASE_QUANTITY("IV002", "예약 재고량보다 더 많이 해제할 수 없습니다."),
    INVALID_COMMIT_QUANTITY("IV003", "전체 예약 수량보다 더 많이 확정할 수 없습니다.");
    private final String name;
    private final String message;
}
