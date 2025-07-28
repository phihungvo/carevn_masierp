package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Currency.
 */
@Data
@Table("currency")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Currency implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("symbol")
    private String symbol;

    @Column("is_active")
    private Boolean isActive;

    @Column("rate")
    private BigDecimal rate;

    @Column("attributes")
    private String attributes;

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

    public Currency id(UUID id) {
        this.setId(id);
        return this;
    }

    public Currency code(String code) {
        this.setCode(code);
        return this;
    }

    public Currency name(String name) {
        this.setName(name);
        return this;
    }

    public Currency symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public Currency isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public Currency rate(BigDecimal rate) {
        this.setRate(rate);
        return this;
    }

    public Currency attributes(String attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public Currency isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Currency createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Currency createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Currency updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Currency updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Currency deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public Currency deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Currency company(String company) {
        this.setCompany(company);
        return this;
    }

    public Currency department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Currency setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
