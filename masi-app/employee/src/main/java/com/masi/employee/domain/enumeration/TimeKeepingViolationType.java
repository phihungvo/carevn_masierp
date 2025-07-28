package com.masi.employee.domain.enumeration;

/**
 * The TimeKeepingViolationType enumeration.
 */
public enum TimeKeepingViolationType {
    ABSENT_WITHOUT_REQUEST,
    INSUFFICIENT_WORKING_TIME,
    OVERTIME,
    MISSING_CHECKOUT,
    NO_VIOLATION //this value is for checking only and will not be saved into database
}
