package com.masi.logistics.domain.enumeration;

import lombok.Getter;

@Getter
public enum IncomingInvoiceStatus {
    NEW("Chưa hoá đơn"),
    WAITING("waiting"),
    APPROVED("approved"),
    REJECTED("rejected"),
    CANCELLED("Hủy"),
    PAID("Đã hoá đơn"),
    IMPORTED("imported"); // đã nhập kho

    private final String vietnameseName;

    IncomingInvoiceStatus(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String toVietnameseName() {
        return vietnameseName;
    }

}
