package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ProfileAttachment.
 */
@Data
@Table("profile_attachment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAttachment implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("employee_profile_id")
    private UUID employeeProfileId;

    @NotNull(message = "must not be null")
    @Column("type")
    private ProfileAttachmentType type;


    @Column("path")
    private String path;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProfileAttachment id(UUID id) {
        this.setId(id);
        return this;
    }

    public ProfileAttachment employeeProfileId(UUID employeeProfileId) {
        this.setEmployeeProfileId(employeeProfileId);
        return this;
    }

    public ProfileAttachment type(ProfileAttachmentType type) {
        this.setType(type);
        return this;
    }

    public ProfileAttachment path(String path) {
        this.setPath(path);
        return this;
    }

    public ProfileAttachment createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProfileAttachment updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ProfileAttachment isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProfileAttachment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
