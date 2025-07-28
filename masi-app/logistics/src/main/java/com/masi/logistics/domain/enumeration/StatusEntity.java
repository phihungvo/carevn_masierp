package com.masi.logistics.domain.enumeration;

import lombok.Getter;

@Getter
public enum StatusEntity {
    NEW(2, "Mới"),
    WAITING_APPROVED(4, "Đợi duyệt"),
    REJECTED(6, "Từ chối"),
    CANCELLED(8, "Hủy"),
    APPROVED(10, "Đã duyệt"),
    COMPLETED(12, "Đã nhập kho"),;

    private final int value;
    private final String vietnameseName;

    StatusEntity(int value, String vietnameseName) {
        this.value = value;
        this.vietnameseName = vietnameseName;
    }

    public static StatusEntity fromValue(int value) {
        for (StatusEntity status : StatusEntity.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static String getVietnameseNameFromValue(int value) {
        for (StatusEntity status : StatusEntity.values()) {
            if (status.value == value) {
                return status.getVietnameseName();
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
