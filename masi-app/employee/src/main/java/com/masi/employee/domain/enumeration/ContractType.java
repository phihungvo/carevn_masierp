package com.masi.employee.domain.enumeration;

/**
 * The ContractType enumeration.
 */
public enum ContractType {
    TRIAL,
    FIXED_TERM,
    UNDEFINED_TERM,
    SEASONAL;
    // Thử việc, Xác định thời hạn, Không xác định thời hạn, Thời vụ

    public static ContractType fromVietnamese(String vietnamese) {
        vietnamese = vietnamese.toLowerCase().trim();
        return switch (vietnamese) {
            case "thử việc" -> TRIAL;
            case "xác định thời hạn" -> FIXED_TERM;
            case "không xác định thời hạn" -> UNDEFINED_TERM;
            case "thời vụ" -> SEASONAL;
            case "" -> null;
            default -> throw new IllegalArgumentException("Unknown contract type: " + vietnamese);
        };
    }

    public String toVietnamese() {
        return switch (this) {
            case TRIAL -> "Thử việc";
            case FIXED_TERM -> "Xác định thời hạn";
            case UNDEFINED_TERM -> "Không xác định thời hạn";
            case SEASONAL -> "Thời vụ";
            default -> null;
        };
    }
}
