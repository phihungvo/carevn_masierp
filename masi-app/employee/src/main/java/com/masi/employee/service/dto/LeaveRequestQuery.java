package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;
import java.util.UUID;

@Data
@ParameterObject
public class LeaveRequestQuery {

    private List<LeaveType> type;
    private List<LeaveRequestStatus> status;
    private WorkspaceType workspaceType;
    private List<UUID> workspaceIds;
    private List<UUID> employeeIds;

}
