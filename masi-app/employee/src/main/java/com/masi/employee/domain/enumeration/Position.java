package com.masi.employee.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * The Position enumeration.
 */
public enum Position {
    EMPLOYEE,
    TEAM_LEADER,
    SUPERVISOR,
    DIRECTOR,
    DEPARTMENT_DIRECTOR,
    // NEW
    EMPLOYEE_OFFICER,
    EMPLOYEE_FACTORY

    ;

    public String toVietnamese() {
        return switch (this) {
            case EMPLOYEE -> "Nhân viên (NV)";
            case TEAM_LEADER -> "Trưởng bộ phận (TBP)";
            case SUPERVISOR -> "Giám sát";
            case DIRECTOR -> "Giám đốc (GĐ)";
            case DEPARTMENT_DIRECTOR -> "Giám đốc bộ phận (GĐBP)";
            // NEW
             case EMPLOYEE_OFFICER -> "Nhân viên văn phòng";
             case EMPLOYEE_FACTORY -> "Nhân viên nhà máy";
        };
    }

    public String toRole() {
        return switch (this) {
            case EMPLOYEE,EMPLOYEE_FACTORY,EMPLOYEE_OFFICER -> "ROLE_USER";
            case TEAM_LEADER, DEPARTMENT_DIRECTOR -> "ROLE_DEPARTMENT_MANAGER";
            case SUPERVISOR -> "ROLE_DEPARTMENT_MANAGER";
            case DIRECTOR -> "ROLE_DIRECTOR";
        };
    }

    public static Position fromVietnamese(String value) {
        if (StringUtils.isBlank(value))
            return null;
        value = value.toLowerCase().trim();
        if (value.startsWith("nhân viên")) {
            return EMPLOYEE;
        } else if (value.startsWith("trưởng bộ phận") || value.startsWith("tbp")) {
            return TEAM_LEADER;
        } else if (value.startsWith("giám sát")) {
            return SUPERVISOR;
        } else if (value.startsWith("giám đốc")) {
            return DIRECTOR;
        } else if (value.startsWith("giám đốc bộ phận")) {
            return DEPARTMENT_DIRECTOR;
        } else {
            throw new IllegalStateException("Unexpected value: " + value);
        }

    }
}
