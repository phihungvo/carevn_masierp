package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.WorkCenterDTO;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WorkCenter.
 */
@Data
@Table("work_center")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkCenter implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("code")
    private String code;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("status")
    private WorkCenterStatusEnum status;

    @NotNull(message = "must not be null")
    @Column("company_id")
    private UUID companyId;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Column("last_checked_at")
    private ZonedDateTime lastCheckedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WorkCenter id(UUID id) {
        this.setId(id);
        return this;
    }

    public WorkCenter name(String name) {
        this.setName(name);
        return this;
    }

    public WorkCenter status(WorkCenterStatusEnum status) {
        this.setStatus(status);
        return this;
    }

    public WorkCenter companyId(UUID companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public WorkCenter createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public WorkCenter createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public WorkCenter updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public WorkCenter updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public WorkCenter isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public WorkCenter deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public WorkCenter deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public WorkCenter setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public WorkCenterDTO toDto() {
        WorkCenterDTO dto = new WorkCenterDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setStatus(this.status);
        dto.setCompanyId(this.companyId);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(updatedAt);
        dto.setLastCheckedAt(this.lastCheckedAt);
        return dto;
    }
}
