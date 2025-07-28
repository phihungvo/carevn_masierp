package com.masi.logistics.domain.enumeration;

/**
 * The RequestStatus enumeration.
 */
public enum RequestStatus {
    NEW("Mới"),
    WAITING_APPROVE("Đợi duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Từ chối"),
    IN_PROGRESS("Đang tiến hành"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Hủy");

    private final String vietnameseName;

    RequestStatus(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String toVietnameseName() {
        return vietnameseName;
    }
}
