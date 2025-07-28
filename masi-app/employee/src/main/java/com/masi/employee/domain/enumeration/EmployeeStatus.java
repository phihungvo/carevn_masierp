package com.masi.employee.domain.enumeration;

/**
 * The EmployeeStatus enumeration.
 */
public enum EmployeeStatus {
    WORKING,
    RESIGNED;

    public String toVietnamese() {
        return switch (this) {
            case WORKING -> "Đang làm việc";
            case RESIGNED -> "Đã nghỉ việc";

        };
    }

    public static EmployeeStatus fromVietnamese(String value) {
        value = value.toLowerCase();
        return switch (value) {
            case "đang làm việc" -> WORKING;
            case "đã nghỉ việc" -> RESIGNED;
            case "" -> null;
            default -> throw new IllegalArgumentException("Unknown value: " + value);
        };
    }
}
