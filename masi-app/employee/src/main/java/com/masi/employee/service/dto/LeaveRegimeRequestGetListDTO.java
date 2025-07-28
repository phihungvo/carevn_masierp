package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;

import com.masi.employee.domain.enumeration.WorkspaceType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.!1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRegimeRequestGetListDTO implements Serializable {

    private List<LeaveType> leaveType;

    private LocalDate startDate;

    private LocalDate endDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String companyId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID employeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

//    private WorkspaceType workspaceType;
    private List<LeaveRegimeRequestStatus> status;


}
