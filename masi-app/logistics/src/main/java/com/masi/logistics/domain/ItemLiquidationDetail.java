package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A ItemLiquidationDetail.
 */
@Data
@Table("item_liquidation_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemLiquidationDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("attribute")
    private String attribute;

    @Column("name")
    private String name;

    @Column("liquidation_date")
    private ZonedDateTime liquidationDate;

    @Column("description")
    private String description;

    @Column("item_liquidation_id")
    private UUID itemLiquidationId;

    @Column("inventories_storage_id")
    private UUID inventoriesStorageId;

    @Transient
    private InventoriesStorage inventoriesStorage;

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

    public ItemLiquidationDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemLiquidationDetail code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemLiquidationDetail attribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemLiquidationDetail name(String name) {
        this.setName(name);
        return this;
    }

    public ItemLiquidationDetail liquidationDate(ZonedDateTime liquidationDate) {
        this.setLiquidationDate(liquidationDate);
        return this;
    }

    public ItemLiquidationDetail description(String description) {
        this.setDescription(description);
        return this;
    }

    public ItemLiquidationDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemLiquidationDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemLiquidationDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemLiquidationDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemLiquidationDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemLiquidationDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemLiquidationDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemLiquidationDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemLiquidationDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemLiquidationDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
