package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.TypeFactory;
import io.r2dbc.postgresql.codec.Json;
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
 * A Factories.
 */
@Data
@Table("factories")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factories implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("type_factory")
    private TypeFactory typeFactory;

    @Column("department_id")
    private UUID departmentId;

    @Column("description")
    private String description;

    @Column("can_delete")
    private Boolean canDelete;

    @Column("normalized_name")
    private String normalizedName;

    @Column("address")
    private String address;

    @Column("employee_owner_id")
    private UUID employeeOwnerId;

    @Column("attribute")
    private Json attribute;

    @Column("note")
    private String note;

    @Column("is_active")
    private Boolean isActive;

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

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Factories id(UUID id) {
        this.setId(id);
        return this;
    }

    public Factories code(String code) {
        this.setCode(code);
        return this;
    }

    public Factories name(String name) {
        this.setName(name);
        return this;
    }

    public Factories typeFactory(TypeFactory typeFactory) {
        this.setTypeFactory(typeFactory);
        return this;
    }

    public Factories departmentId(UUID departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public Factories description(String description) {
        this.setDescription(description);
        return this;
    }

    public Factories canDelete(Boolean canDelete) {
        this.setCanDelete(canDelete);
        return this;
    }

    public Factories normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public Factories address(String address) {
        this.setAddress(address);
        return this;
    }

    public Factories employeeOwnerId(UUID employeeOwnerId) {
        this.setEmployeeOwnerId(employeeOwnerId);
        return this;
    }

    public Factories attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public Factories note(String note) {
        this.setNote(note);
        return this;
    }

    public Factories isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public Factories company(String company) {
        this.setCompany(company);
        return this;
    }

    public Factories department(String department) {
        this.setDepartment(department);
        return this;
    }

    public Factories isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Factories createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Factories createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Factories updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Factories updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Factories deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Factories deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Factories setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
