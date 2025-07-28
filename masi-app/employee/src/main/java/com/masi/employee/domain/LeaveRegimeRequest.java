package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveType;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A LeaveRegimeRequest.
 */
@Table("leave_regime_request")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class LeaveRegimeRequest implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("leave_type")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    @Column("last_work_date")
    private ZonedDateTime lastWorkDate;

    @NotNull(message = "must not be null")
    @Column("return_work_date")
    private ZonedDateTime returnWorkDate;

    @Column("from_time")
    private ZonedDateTime fromTime;

    @Column("to_time")
    private ZonedDateTime toTime;

    @Column("substitute_id")
    private UUID substituteId;

    @Column("file_id")
    private String fileId;

    @Column("files")
    private Json files;

    @Column("file_name")
    private String fileName;

    @Column("company_id")
    private String companyId;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @Column("leave_request_day_type")
    private LeaveRequestDayType leaveRequestDayType;

    @Column("status")
    private LeaveRegimeRequestStatus status;

    @Column("leave_request_id")
    private UUID leaveRequestId;

    @Column("department")
    private String department;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private UUID createdBy;

    @Column("updated_by")
    private UUID updatedBy;

    @Column("deleted_by")
    private UUID deletedBy;
    @Column("total_day_off")
    private Float totalDayOff = 0f;
    @Transient
    private boolean isPersisted;

    @Transient
    private Employee substitute;

    @Transient
    private Employee employee;

    @Transient
    private Set<ProcessLeaveRegimeRequest> processLeaveRegimeRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveRegimeRequest id(UUID id) {
        this.setId(id);
        return this;
    }


    public LeaveRegimeRequest leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public LeaveRegimeRequest lastWorkDate(ZonedDateTime lastWorkDate) {
        this.setLastWorkDate(lastWorkDate);
        return this;
    }

    public LeaveRegimeRequest returnWorkDate(ZonedDateTime returnWorkDate) {
        this.setReturnWorkDate(returnWorkDate);
        return this;
    }

    public LeaveRegimeRequest substituteId(UUID substituteId) {
        this.setSubstituteId(substituteId);
        return this;
    }

    public LeaveRegimeRequest companyId(String companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public LeaveRegimeRequest employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public LeaveRegimeRequest leaveRequestDayType(LeaveRequestDayType leaveRequestDayType) {
        this.setLeaveRequestDayType(leaveRequestDayType);
        return this;
    }

    public LeaveRegimeRequest status(LeaveRegimeRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public LeaveRegimeRequest leaveRequestId(UUID leaveRequestId) {
        this.setLeaveRequestId(leaveRequestId);
        return this;
    }

    public LeaveRegimeRequest createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public LeaveRegimeRequest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public LeaveRegimeRequest deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public LeaveRegimeRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public LeaveRegimeRequest createdBy(UUID createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public LeaveRegimeRequest updatedBy(UUID updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public LeaveRegimeRequest deletedBy(UUID deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public LeaveRegimeRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
