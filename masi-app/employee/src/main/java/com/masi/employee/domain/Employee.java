package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.EmployeeDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The Employee entity.
 */
@Data
@Table("employee")
@JsonIgnoreProperties(value = {"new"})
@Builder
@AllArgsConstructor
@NoArgsConstructor

@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The firstname attribute.
     */
    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("full_name")
    private String fullName;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("hire_date")
    private ZonedDateTime hireDate;

    @Column("salary")
    private Long salary;

    @Column("commission_pct")
    private Long commissionPct;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("workspace_id")
    private UUID workspaceId;

    @Transient
    private boolean isPersisted;

    @Transient
    private List<TimeKeeping> timeKeepings;

    @Transient
    private Workspace workspace;

    @Transient
    private EmployeeProfile employeeProfile;


    public String getFullName() {
        if (Objects.isNull(this.firstName) || Objects.isNull(this.lastName)) {
            return "";
        }
        return this.firstName + " " + this.lastName;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Employee id(UUID id) {
        this.setId(id);
        return this;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public Employee hireDate(ZonedDateTime hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public Employee salary(Long salary) {
        this.setSalary(salary);
        return this;
    }

    public Employee commissionPct(Long commissionPct) {
        this.setCommissionPct(commissionPct);
        return this;
    }

    public Employee isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public Employee workspaceName(UUID workspaceId) {
        this.setWorkspaceId(workspaceId);
        return this;
    }

    public Employee workspace(Workspace workspace) {
        this.setWorkspace(workspace);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Employee setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public EmployeeDTO toDto() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(this.id);
        employeeDTO.setFirstName(this.firstName);
        employeeDTO.setLastName(this.lastName);
        if (Objects.nonNull(this.timeKeepings)) {
            employeeDTO.setTimeKeepings(this.timeKeepings.stream().map(TimeKeeping::toDto).toList());
        }
        if (Objects.nonNull(this.employeeProfile)) {
            employeeDTO.setCode(this.employeeProfile.getEmployeeCode());
            employeeDTO.setEmployeeProfile(this.employeeProfile.toBriefDTO());
        }
        if(Objects.nonNull(workspace)) {
            employeeDTO.setWorkspace(this.workspace.toDTO());
        }
        return employeeDTO;
    }

}
