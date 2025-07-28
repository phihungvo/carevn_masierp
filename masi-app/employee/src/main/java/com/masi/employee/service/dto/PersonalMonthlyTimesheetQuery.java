package com.masi.employee.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Data
public class PersonalMonthlyTimesheetQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 23242424L;

    private LocalDate month;

    private TimeKeepingType type = TimeKeepingType.HOUR;
    private WorkspaceType workspaceType = WorkspaceType.OFFICE;
    private String company;
    private Collection<UUID> workspaces;
}
