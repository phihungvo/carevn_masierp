package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.EmployeeDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The Employee entity.
 */

@EqualsAndHashCode(callSuper = true)
@Table("employee")
@JsonIgnoreProperties(value = {"new"})
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeJoinCode extends Employee implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Column("employee_code")
    private String employeeCode;

    @Column("workspace_name")
    private String workspaceName;

    @Column("workspace_normalized_name")
    private String workspaceNormalizedName;


    public EmployeeDTO toDto() {
        EmployeeDTO dto = super.toDto();
        dto.setEmployeeCode(this.getEmployeeCode());
        dto.setWorkspaceName(this.getWorkspaceName());
        dto.setWorkspaceNormalizedName(this.getWorkspaceNormalizedName());
        return dto;
    }
}
