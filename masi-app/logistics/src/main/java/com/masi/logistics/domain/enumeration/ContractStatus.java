package com.masi.logistics.domain.enumeration;

/**
 * The ContractStatus enumeration.
 */
public enum ContractStatus {
    NEW,
    WAITING_APPROVE,
    // CHỜ THANH LÝ
    WAITING_LIQUIDATION,
    // ĐÃ DUYỆT
    APPROVED,
    // TỪ CHỐI
    REJECTED,
    // TỪ CHỐI THANH LÝ
    REJECTED_LIQUIDATION,
    // ĐÃ THANH LÝ
    LIQUIDATED,
    // HẾT HẠN
    EXPIRED,
    // ĐÃ HỦY
    CANCELLED;
//    // Hoàn thành
//    COMPLETED,

    public enum DeliveryStatus{
        NOT_DELIVERED,
        DELIVERING,
        DELIVERED
    }

}
