package com.masi.employee.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * The Gender enumeration.
 */
public enum Gender {
    FEMALE,
    MALE,
    NO_PREFERENCE;

    public String toVietnamese() {
        return switch (this) {
            case FEMALE -> "Nữ";
            case MALE -> "Nam";
            case NO_PREFERENCE -> "Không yêu cầu";
        };
    }

    public static Gender formVietnamese(String value) {//nam/nu
        value = StringUtils.stripAccents(value).toLowerCase();
        return switch (value) {
            case "nam" -> MALE;
            case "nu" -> FEMALE;
            case "" -> null;
            default -> throw new IllegalArgumentException("invalid gender");
        };
    }
}
