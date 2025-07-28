package com.masi.employee.domain.enumeration;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The LeaveType enumeration.
 */
public enum LeaveType {
    SICK_LEAVE,
    ANNUAL_LEAVE,
    MATERNITY_LEAVE,
    UNPAID_LEAVE,
    FUNERAL_LEAVE,
    WEDDING_LEAVE,
    COMPENSATION_LEAVE,
    ANNUAL_LEAVE_FULL_DAY,
    ANNUAL_LEAVE_HALF_DAY;

    public static final Map<LeaveType, String> VIETNAMESE_MAP = new EnumMap<>(LeaveType.class);

    static {
        VIETNAMESE_MAP.put(SICK_LEAVE, "Nghỉ bệnh");
        VIETNAMESE_MAP.put(ANNUAL_LEAVE, "Nghỉ thường niên");
        VIETNAMESE_MAP.put(MATERNITY_LEAVE, "Nghỉ thai sản");
        VIETNAMESE_MAP.put(UNPAID_LEAVE, "Nghỉ không lương");
        VIETNAMESE_MAP.put(FUNERAL_LEAVE, "Nghỉ tang");
        VIETNAMESE_MAP.put(WEDDING_LEAVE, "Nghỉ cưới");
        VIETNAMESE_MAP.put(COMPENSATION_LEAVE, "Nghỉ bù");
    }



    public static List<LeaveType> getLeaveTypes() {
        return List.of(SICK_LEAVE, ANNUAL_LEAVE, MATERNITY_LEAVE, UNPAID_LEAVE, FUNERAL_LEAVE, WEDDING_LEAVE, COMPENSATION_LEAVE);
    }


}


