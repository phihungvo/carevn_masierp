package com.masi.production.domain.enumeration;

/**
 * The MoStatus enumeration.
 */
public enum MoStatus {
    CREATED,
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    NEW,
    COMPLETE_PRODUCTION, // xong sản xuất
    TRANSPORTING, // đang vẫn chuyển
    WAREHOUSED, // đã nhập kho
    PAUSED, // tạm dừng
}
