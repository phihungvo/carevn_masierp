package com.masi.employee.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExplanationRO extends RequestObjectBase{
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<String> types;
    private Boolean isAbnormal;
    private Boolean isLocked;



    private Collection<UUID> workspaceIds;
    private Collection<UUID> employeeIds;
    private LocalDate startFrom;
    private LocalDate startTo;


    public Boolean getAbnormal() {
        return isAbnormal;
    }

    public void setAbnormal(Boolean abnormal) {
        isAbnormal = abnormal;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

}
