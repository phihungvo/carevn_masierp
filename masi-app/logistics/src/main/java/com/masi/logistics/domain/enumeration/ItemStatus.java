package com.masi.logistics.domain.enumeration;

public enum ItemStatus {
    DEPRECIATION("Không KH/PB"),
    ACTIVE("Hoạt động"),
    LIQUIDATION("Thanh lý"),
    SAVE_STORAGE("Lưu kho"),
    CANCEL("Hủy");

    private final String vietnameseName;

    // Constructor
    ItemStatus(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    // Getter for Vietnamese name
    public String getVietnameseName() {
        return vietnameseName;
    }
}
