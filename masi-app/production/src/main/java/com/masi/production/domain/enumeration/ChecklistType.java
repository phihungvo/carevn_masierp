package com.masi.production.domain.enumeration;
// public static final String MIXING_REPORT_CHECKLIST= "bao cao tron san pham bot ca";
// public static final String ADDITIVE_MATERIAL_CHECKLIST= "lua va bo sung phu gia";
// public static final String MACHINE_OPERATION_CHECKLIST= "giam sat hoat dong may";
// public static final String METAL_DETECTION_CHECKLIST= "kiem tra nam cham va luoi";
// public static final String STEAMING_PROCESS_CHECKLIST = "giam sat cong doan hap - say";
// public static final String RECEIVE_MATERIAL_CHECKLIST = "biam sat tiep nhan nguyen lieu";

import org.apache.commons.lang3.StringUtils;

public enum ChecklistType {
    MIXING_REPORT_CHECKLIST("bao cao tron san pham bot ca"),
    ADDITIVE_MATERIAL_CHECKLIST("lua va bo sung phu gia"),
    MACHINE_OPERATION_CHECKLIST("giam sat hoat dong may"),
    METAL_DETECTION_CHECKLIST("kiem tra nam cham va luoi"),
    STEAMING_PROCESS_CHECKLIST("giam sat cong doan hap - say"),
    RECEIVE_MATERIAL_CHECKLIST("biam sat tiep nhan nguyen lieu");
    
    private final String value;

    ChecklistType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
    public boolean isLike(String other) {
        return this.value.contains(StringUtils.stripAccents(other).toLowerCase());
    }
}