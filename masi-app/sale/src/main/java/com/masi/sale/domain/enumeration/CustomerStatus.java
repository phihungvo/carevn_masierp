package com.masi.sale.domain.enumeration;

/**
 * The CustomerStatus enumeration.
 */
public enum CustomerStatus {
    ENABLED,
    DISABLED,
    FOWARDED;

    public String toVietnamese() {
        return switch (this) {
            case ENABLED -> "Hoạt động";
            case DISABLED -> "Vô hiệu";
            case FOWARDED -> "Chuyển tiếp";
        };
    }
}
