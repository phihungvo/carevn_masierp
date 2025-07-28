package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;
import com.masi.employee.service.dto.EmployeeDTO;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ProcessLeaveRegimeRequest.
 */
@Table("process_leave_regime_request")
@Data
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessLeaveRegimeRequest implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("leave_regime_request_id")
    private UUID leaveRegimeRequestId;

    @Column("status")
    private ProcessLeaveRegimeRequestStatus status;

    @Column("employee_id")
    private UUID employeeId;

    @Column("approver_id")
    private UUID approverId;

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

    @Column("reason")
    private String reason;

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

    @Transient
    private boolean isPersisted;

    @Transient
    private Employee employee;

    @Transient
    private Employee approver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProcessLeaveRegimeRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public ProcessLeaveRegimeRequest leaveRegimeRequestId(UUID leaveRegimeRequestId) {
        this.setLeaveRegimeRequestId(leaveRegimeRequestId);
        return this;
    }

    public ProcessLeaveRegimeRequest status(ProcessLeaveRegimeRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public ProcessLeaveRegimeRequest employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public ProcessLeaveRegimeRequest approverId(UUID approverId) {
        this.setApproverId(approverId);
        return this;
    }

    public ProcessLeaveRegimeRequest reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public ProcessLeaveRegimeRequest createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProcessLeaveRegimeRequest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ProcessLeaveRegimeRequest deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ProcessLeaveRegimeRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ProcessLeaveRegimeRequest createdBy(UUID createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ProcessLeaveRegimeRequest updatedBy(UUID updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ProcessLeaveRegimeRequest deletedBy(UUID deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProcessLeaveRegimeRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

}
