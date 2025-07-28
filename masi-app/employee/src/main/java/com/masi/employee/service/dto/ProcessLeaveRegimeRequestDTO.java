package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.ProcessLeaveRegimeRequest}
 * entity.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessLeaveRegimeRequestDTO implements Serializable {

    private UUID id;

    private UUID leaveRegimeRequestId;

    private ProcessLeaveRegimeRequestStatus status;

    private UUID employeeId;

    private UUID approverId;

    private String fileId;

    private String fileName;

    private String reason;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deletedAt;

    private Boolean isDeleted;

    private UUID createdBy;

    private UUID updatedBy;

    private UUID deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = Employee.class)
    private EmployeeDTO approver;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = Employee.class)
    private EmployeeDTO employee;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processLeaveRegimeRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessLeaveRegimeRequestDTO{" +
                "id='" + getId() + "'" +
                ", leaveRegimeRequestId='" + getLeaveRegimeRequestId() + "'" +
                ", status='" + getStatus() + "'" +
                ", employeeId='" + getEmployeeId() + "'" +
                ", approverId='" + getApproverId() + "'" +
                ", reason='" + getReason() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", deletedAt='" + getDeletedAt() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                ", createdBy='" + getCreatedBy() + "'" +
                ", updatedBy='" + getUpdatedBy() + "'" +
                ", deletedBy='" + getDeletedBy() + "'" +
                "}";
    }

    public ProcessLeaveRegimeRequestDTO init(UUID leaveRegimeRequestId, UUID employeeId, UUID approverId) {
        this.id = UUID.randomUUID();
        this.leaveRegimeRequestId = leaveRegimeRequestId;
        this.employeeId = employeeId;
        this.approverId = approverId;
        this.status = ProcessLeaveRegimeRequestStatus.WAITING_APPROVAL;
        this.createdAt = ZonedDateTime.now();
        this.createdBy = employeeId;
        return this;
    }
}
