package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A LeaveDay.
 */
@Data
@Table("leave_day")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveDay implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("leave_request_id")
    private UUID leaveRequestId;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    @Column("date")
    private LocalDate date;

    @NotNull(message = "must not be null")
    @Column("leave_request_day_type")
    private LeaveRequestDayType leaveRequestDayType;

    @NotNull(message = "must not be null")
    @Column("leave_type")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    @Column("is_locked")
    private Boolean isLocked;

    @Transient
    private boolean isPersisted;

    @JsonIgnore
    public boolean isPaidLeave() {
        return this.leaveType == LeaveType.ANNUAL_LEAVE ||
            this.leaveType == LeaveType.MATERNITY_LEAVE ||
            this.leaveType == LeaveType.WEDDING_LEAVE ||
            this.leaveType == LeaveType.FUNERAL_LEAVE ||
            this.leaveType == LeaveType.SICK_LEAVE;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveDay id(UUID id) {
        this.setId(id);
        return this;
    }

    public LeaveDay leaveRequestId(UUID leaveRequestId) {
        this.setLeaveRequestId(leaveRequestId);
        return this;
    }

    public LeaveDay employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public LeaveDay date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public LeaveDay leaveRequestDayType(LeaveRequestDayType leaveRequestDayType) {
        this.setLeaveRequestDayType(leaveRequestDayType);
        return this;
    }

    public LeaveDay leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public LeaveDay isLocked(Boolean isLocked) {
        this.setIsLocked(isLocked);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public LeaveDay setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
