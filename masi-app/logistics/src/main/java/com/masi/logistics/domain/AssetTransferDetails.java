package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AssetTransferDetails.
 */
@Data
@Table("asset_transfer_details")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetTransferDetails implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("item_asset_transfer_id")
    private UUID itemAssetTransferId;

    @Transient
    @JsonIgnoreProperties(value = { "assetTransferDetails" }, allowSetters = true)
    private ItemAssetTransfer itemAssetTransfer;

    @Column("attribute")
    private Json attribute;

    @Column("name")
    private String name;

    @Column("status")
    private String status;

    @Column("inventories_storage_id")
    private UUID inventoriesStorageId;

    @Column("employee_to_id")
    private UUID employeeToId;

    @Column("employee_from_id")
    private UUID employeeFromId;

    @Transient
    @JsonIgnoreProperties(value = { "item", "inventoriesStorage", "transactionType" }, allowSetters = true)
    private InventoriesStorage inventoriesStorage;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("notes")
    private String notes;

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

    public AssetTransferDetails id(UUID id) {
        this.setId(id);
        return this;
    }

    public AssetTransferDetails code(String code) {
        this.setCode(code);
        return this;
    }

    public AssetTransferDetails attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public AssetTransferDetails name(String name) {
        this.setName(name);
        return this;
    }

    public AssetTransferDetails status(String status) {
        this.setStatus(status);
        return this;
    }

    public AssetTransferDetails inventoriesStorageId(UUID inventoriesStorageId) {
        this.setInventoriesStorageId(inventoriesStorageId);
        return this;
    }

    public AssetTransferDetails quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public AssetTransferDetails notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public AssetTransferDetails isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public AssetTransferDetails createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public AssetTransferDetails createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public AssetTransferDetails updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public AssetTransferDetails updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public AssetTransferDetails deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public AssetTransferDetails deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public AssetTransferDetails company(String company) {
        this.setCompany(company);
        return this;
    }

    public AssetTransferDetails department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AssetTransferDetails setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
