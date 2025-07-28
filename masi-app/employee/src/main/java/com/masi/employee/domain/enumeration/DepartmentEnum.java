package com.masi.employee.domain.enumeration;

public enum DepartmentEnum {
    HR("HR"),
    IT("IT"),
    FINANCE("FINANCE"),
    MARKETING("MARKETING"),
    SALES("SALES"),
    PRODUCTION("PRODUCTION"),
    LOGISTICS("LOGISTICS"),
    ADMINISTRATION("ADMINISTRATION"),
    RESEARCH_AND_DEVELOPMENT("RESEARCH_AND_DEVELOPMENT"),
    CUSTOMER_SERVICE("CUSTOMER_SERVICE"),
    QUALITY_CONTROL("QUALITY_CONTROL"),
    PURCHASING("PURCHASING"),
    ACCOUNTING("ACCOUNTING"),
    LEGAL("LEGAL"),
    MANAGEMENT("MANAGEMENT"),
    
    OTHER("OTHER");

    private String key;

    DepartmentEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
