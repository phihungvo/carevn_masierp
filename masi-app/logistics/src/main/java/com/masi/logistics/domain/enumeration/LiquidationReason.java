package com.masi.logistics.domain.enumeration;

import lombok.Getter;

@Getter
public enum LiquidationReason  {
    LOST("Mất"),
    CANCELED("Huỷ"),
    BROKEN("Bể"),
    SOLD("Bán"),
    CONTRIBUTED("Góp vốn"),
    EXPIRED("Hết hạn"),
    TRANSFERRED("Giảm chuyển kho");

    // Getter for Vietnamese value
    private final String vietnameseValue;

    // Constructor
    LiquidationReason(String vietnameseValue) {
        this.vietnameseValue = vietnameseValue;
    }

}
