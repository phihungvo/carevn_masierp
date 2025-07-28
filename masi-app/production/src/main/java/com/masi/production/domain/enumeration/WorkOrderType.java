package com.masi.production.domain.enumeration;

import java.util.Map;

public enum WorkOrderType {
    ADDITIVE_MATERIAL_CHECKLIST,
    MACHINE_OPERATION_CHECKLIST,
    METAL_DETECTION_CHECKLIST,
    STEAMING_PROCESS_CHECKLIST,
    RECEIVE_MATERIAL_CHECKLIST,
    MIXING_REPORT_CHECKLIST,
    ;
    public static final Map<WorkOrderType, Integer> WORK_ORDER_TYPE_STANDARD = Map.of(
        RECEIVE_MATERIAL_CHECKLIST, 2,
        ADDITIVE_MATERIAL_CHECKLIST, 4,
        MACHINE_OPERATION_CHECKLIST, 6,
        STEAMING_PROCESS_CHECKLIST, 8,
        METAL_DETECTION_CHECKLIST, 10
//        MIXING_REPORT_CHECKLIST, 6
    );

    public static final Map<WorkOrderType, Integer> WORK_ORDER_TYPE_ORDER = Map.of(
        MIXING_REPORT_CHECKLIST, 1,
        ADDITIVE_MATERIAL_CHECKLIST, 2
    );

    public Integer getOrder() {
        return WORK_ORDER_TYPE_STANDARD.getOrDefault(this, 10);

    }

    public Integer getStandardOrder() {
        return WORK_ORDER_TYPE_ORDER.getOrDefault(this, 10);

    }
}
