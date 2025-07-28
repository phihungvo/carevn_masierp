package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.logistics.domain.enumeration.LiquidationReason;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemLiquidation.
 */
@Data
@Table("item_liquidation")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemLiquidation implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_NAME = "itemLiquidation";

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("attribute")
    private Json attribute;

    @Column("name")
    private String name;

    @Column("status")
    private StatusEntity status;

    @Column("liquidation_date")
    private ZonedDateTime liquidationDate;

    @Column("description")
    private String description;

    @Column("reason")
    private LiquidationReason reason;

    @Column("personnel_list")
    private Json personnelList;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_at")
    private ZonedDateTime createdAt;

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

    public ItemLiquidation id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemLiquidation code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemLiquidation attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemLiquidation name(String name) {
        this.setName(name);
        return this;
    }

    public ItemLiquidation status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public ItemLiquidation liquidationDate(ZonedDateTime liquidationDate) {
        this.setLiquidationDate(liquidationDate);
        return this;
    }

    public ItemLiquidation description(String description) {
        this.setDescription(description);
        return this;
    }

    public ItemLiquidation reason(LiquidationReason reason) {
        this.setReason(reason);
        return this;
    }

    public ItemLiquidation personnelList(Json personnelList) {
        this.setPersonnelList(personnelList);
        return this;
    }

    public ItemLiquidation isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemLiquidation createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemLiquidation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemLiquidation updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemLiquidation updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemLiquidation deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemLiquidation deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemLiquidation company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemLiquidation department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemLiquidation setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


}
