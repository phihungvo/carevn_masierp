package com.masi.logistics.domain;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
 * A SuppliesRequestType.
 */
@Data
@Table("supplies_request_type")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesRequestType implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("note")
    private String note;

    @Column("attribute")
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

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

    public SuppliesRequestType id(UUID id) {
        this.setId(id);
        return this;
    }

    public SuppliesRequestType code(String code) {
        this.setCode(code);
        return this;
    }

    public SuppliesRequestType name(String name) {
        this.setName(name);
        return this;
    }

    public SuppliesRequestType note(String note) {
        this.setNote(note);
        return this;
    }

    public SuppliesRequestType attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public SuppliesRequestType isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public SuppliesRequestType createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public SuppliesRequestType createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public SuppliesRequestType updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public SuppliesRequestType updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public SuppliesRequestType deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public SuppliesRequestType deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public SuppliesRequestType company(String company) {
        this.setCompany(company);
        return this;
    }

    public SuppliesRequestType department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SuppliesRequestType setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
