package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

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
 * A InventoriesType.
 */
@Data
@Table("inventories_type")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesType implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("is_active")
    private Boolean isActive;

    @JsonIgnore
    @Column("is_deleted")
    private Boolean isDeleted;

    @JsonIgnore
    @Column("created_at")
    private ZonedDateTime createdAt;

    @JsonIgnore
    @Column("created_by")
    private String createdBy;

    @JsonIgnore
    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @JsonIgnore
    @Column("updated_by")
    private String updatedBy;

    @JsonIgnore
    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @JsonIgnore
    @Column("deleted_by")
    private String deletedBy;

    @JsonIgnore
    @Column("company")
    private String company;

    @JsonIgnore
    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InventoriesType id(UUID id) {
        this.setId(id);
        return this;
    }

    public InventoriesType code(String code) {
        this.setCode(code);
        return this;
    }

    public InventoriesType name(String name) {
        this.setName(name);
        return this;
    }

    public InventoriesType description(String description) {
        this.setDescription(description);
        return this;
    }

    public InventoriesType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public InventoriesType isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InventoriesType createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InventoriesType createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InventoriesType updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InventoriesType updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InventoriesType deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InventoriesType deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InventoriesType company(String company) {
        this.setCompany(company);
        return this;
    }

    public InventoriesType department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InventoriesType setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
