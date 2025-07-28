package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import com.masi.employee.service.dto.EmployeeShiftDetailDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmployeeShiftDetail.
 */
@Data
@Table("employee_shift_detail")
@JsonIgnoreProperties(value = { "new" })
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeShiftDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("date")
    private LocalDate date;

    @Column("time_keeping_id")
    private UUID timeKeepingId;

    @Column("shift_id")
    private UUID shiftId;

    @Column("employee_id")
    private UUID employeeId;

    @Column("check_in_time")
    private ZonedDateTime checkInTime;

    @Column("check_out_time")
    private ZonedDateTime checkOutTime;

    @Column("completion_percent")
    private Float completionPercent;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("is_shift_off")
    private Boolean isShiftOff;

    @Column("leave_day_type")
    private LeaveType leaveDayType;

    @Column("is_wfh")
    private Boolean isWfh;

    @Column("violation_id")
    private UUID violationId;

    @Column("violation_type")
    private TimeKeepingViolationType violationType;

    @Column("is_paid_shift")
    private Boolean isPaidShift;

    @Column("note")
    private String note;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeShiftDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public EmployeeShiftDetail date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public EmployeeShiftDetail timeKeepingId(UUID timeKeepingId) {
        this.setTimeKeepingId(timeKeepingId);
        return this;
    }

    public EmployeeShiftDetail checkInTime(ZonedDateTime checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public EmployeeShiftDetail checkOutTime(ZonedDateTime checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public EmployeeShiftDetail completionPercent(Float completionPercent) {
        this.setCompletionPercent(completionPercent);
        return this;
    }

    public EmployeeShiftDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public EmployeeShiftDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    public EmployeeShiftDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public EmployeeShiftDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public EmployeeShiftDetail createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public EmployeeShiftDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public EmployeeShiftDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public EmployeeShiftDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public EmployeeShiftDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public EmployeeShiftDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    protected EmployeeShiftDetailDTO toDto() {
        EmployeeShiftDetailDTO employeeShiftDetailDTO = new EmployeeShiftDetailDTO();
        employeeShiftDetailDTO.setId(this.getId());
        employeeShiftDetailDTO.setDate(this.getDate());
        employeeShiftDetailDTO.setTimeKeepingId(this.getTimeKeepingId());
        employeeShiftDetailDTO.setShiftId(this.getShiftId());
        employeeShiftDetailDTO.setEmployeeId(this.getEmployeeId());
        employeeShiftDetailDTO.setCheckInTime(this.getCheckInTime());
        employeeShiftDetailDTO.setCheckOutTime(this.getCheckOutTime());
        employeeShiftDetailDTO.setCompletionPercent(this.getCompletionPercent());
        employeeShiftDetailDTO.setCompany(this.getCompany());
        employeeShiftDetailDTO.setDepartment(this.getDepartment());
        employeeShiftDetailDTO.setIsDeleted(this.getIsDeleted());
        employeeShiftDetailDTO.setCreatedBy(this.getCreatedBy());
        employeeShiftDetailDTO.setCreatedDate(this.getCreatedDate());
        employeeShiftDetailDTO.setUpdatedBy(this.getUpdatedBy());
        employeeShiftDetailDTO.setUpdatedAt(this.getUpdatedAt());
        employeeShiftDetailDTO.setDeletedBy(this.getDeletedBy());
        employeeShiftDetailDTO.setDeletedAt(this.getDeletedAt());
        employeeShiftDetailDTO.setIsShiftOff(this.getIsShiftOff());
        employeeShiftDetailDTO.setLeaveDayType(this.getLeaveDayType());
        employeeShiftDetailDTO.setIsWfh(this.getIsWfh());
        employeeShiftDetailDTO.setViolationId(this.getViolationId());
        employeeShiftDetailDTO.setViolationType(this.getViolationType());
        employeeShiftDetailDTO.setNote(this.getNote());
        employeeShiftDetailDTO.setIsPaidShift(this.getIsPaidShift());
        return employeeShiftDetailDTO;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
