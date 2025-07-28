package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@ParameterObject
public class TimeKeepingViolationQuery {

    private LocalDate fromDate;
    private LocalDate toDate;
    private List<TimeKeepingViolationType> type;
    private List<UUID> employeeIds;
    private List<UUID> workspaceIds;
    private String company;
    private UUID explanationId;
    private Boolean explained;
}
