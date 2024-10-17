package com.example.demoshop.domain.transaction.common;

import lombok.Getter;

/**
 * CONTACT_PENDING("거래 연락 받는 중"),
 * MEETING_SCHEDULED("약속 잡힘"),
 * TRADE_COMPLETE("거래 완료"),
 */

@Getter
public enum SaleStatus {

    CONTACT_PENDING,
    MEETING_SCHEDULED,
    TRADE_COMPLETE
}
