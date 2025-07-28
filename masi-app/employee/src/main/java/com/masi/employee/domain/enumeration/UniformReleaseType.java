package com.masi.employee.domain.enumeration;

import lombok.Getter;

/**
 * The UniformReleaseType enumeration.
 */
@Getter
public enum UniformReleaseType {
    SALE("Xuất bán"),
    SUPPORT("Xuất hỗ trợ"),
    SENIORITY("Xuất thâm niên"),
    OTHER("Khác");

    private final String vietnameseName;

    UniformReleaseType(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

}
