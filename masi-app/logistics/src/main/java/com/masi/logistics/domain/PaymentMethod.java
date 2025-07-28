package com.masi.logistics.domain;

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
 * A PaymentMethod.
 */
@Data
@Table("payment_method")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentMethod implements Serializable, Persistable<UUID> {

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

    @Column("attributes")
    private String attributes;

    @Column("is_cash")
    private Boolean isCash;

    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PaymentMethod id(UUID id) {
        this.setId(id);
        return this;
    }

    public PaymentMethod code(String code) {
        this.setCode(code);
        return this;
    }

    public PaymentMethod name(String name) {
        this.setName(name);
        return this;
    }

    public PaymentMethod description(String description) {
        this.setDescription(description);
        return this;
    }

    public PaymentMethod isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public PaymentMethod attributes(String attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public PaymentMethod isCash(Boolean isCash) {
        this.setIsCash(isCash);
        return this;
    }

    public PaymentMethod isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public PaymentMethod createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public PaymentMethod createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public PaymentMethod updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public PaymentMethod updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public PaymentMethod deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public PaymentMethod deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public PaymentMethod company(String company) {
        this.setCompany(company);
        return this;
    }

    public PaymentMethod department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PaymentMethod setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
