package com.masi.employee.domain.enumeration;

import lombok.Getter;

/**
 * The PositionEmployee enumeration.
 */
@Getter
public enum PositionEmployee {
    DIRECTOR("Giám đốc"),
    PERSONNEL_MANAGER("Quản lý nhân sự"),
    ACCOUNTING_MANAGER("Quản lý kế toán"),
    DEPARTMENT_MANAGER("Quản lý phòng ban"),
    REVIEWER("Người đánh giá");

    private final String vietnameseName;

    PositionEmployee(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

}
