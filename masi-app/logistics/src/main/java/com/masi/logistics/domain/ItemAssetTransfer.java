package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemAssetTransfer.
 */
@Data
@Table("item_asset_transfer")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetTransfer implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;
    public static final String ENTITY_NAME = "itemAssetTransfer";

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

    @Column("inventories_storage_id")
    private UUID inventoriesStorageId;

    @Transient
    @JsonIgnoreProperties(value = { "itemAssetTransfers" }, allowSetters = true)
    private InventoriesStorage inventoriesStorage;

    @Column("transaction_type_id")
    private UUID transactionTypeId;

    @Transient
    @JsonIgnoreProperties(value = { "itemAssetTransfers" }, allowSetters = true)
    private TransactionType transactionType;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Transient
    @JsonIgnoreProperties(value = { "itemAssetTransfers" }, allowSetters = true)
    private ItemCategory itemCategory;

    @Column("transfer_date")
    private ZonedDateTime transferDate;

    @Column("description")
    private String description;

    @Column("from_unit")
    private String fromUnit;

    @Column("from_department_id")
    private UUID fromDepartmentId;

    @Column("to_department_id")
    private UUID toDepartmentId;

    @Column("from_person_id")
    private UUID fromPersonId;

    @Column("to_person_id")
    private UUID toPersonId;

    @Column("from_address")
    private String fromAddress;

    @Column("to_address")
    private String toAddress;

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

    public ItemAssetTransfer id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemAssetTransfer code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemAssetTransfer attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemAssetTransfer name(String name) {
        this.setName(name);
        return this;
    }

    public ItemAssetTransfer status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public ItemAssetTransfer inventoriesStorageId(UUID inventoriesStorageId) {
        this.setInventoriesStorageId(inventoriesStorageId);
        return this;
    }

    public ItemAssetTransfer transactionTypeId(UUID transactionTypeId) {
        this.setTransactionTypeId(transactionTypeId);
        return this;
    }

    public ItemAssetTransfer itemCategoryId(UUID itemCategoryId) {
        this.setItemCategoryId(itemCategoryId);
        return this;
    }

    public ItemAssetTransfer transferDate(ZonedDateTime transferDate) {
        this.setTransferDate(transferDate);
        return this;
    }

    public ItemAssetTransfer description(String description) {
        this.setDescription(description);
        return this;
    }

    public ItemAssetTransfer fromUnit(String fromUnit) {
        this.setFromUnit(fromUnit);
        return this;
    }

    public ItemAssetTransfer fromDepartmentId(UUID fromDepartmentId) {
        this.setFromDepartmentId(fromDepartmentId);
        return this;
    }

    public ItemAssetTransfer toDepartmentId(UUID toDepartmentId) {
        this.setToDepartmentId(toDepartmentId);
        return this;
    }

    public ItemAssetTransfer fromPersonId(UUID fromPersonId) {
        this.setFromPersonId(fromPersonId);
        return this;
    }

    public ItemAssetTransfer toPersonId(UUID toPersonId) {
        this.setToPersonId(toPersonId);
        return this;
    }

    public ItemAssetTransfer fromAddress(String fromAddress) {
        this.setFromAddress(fromAddress);
        return this;
    }

    public ItemAssetTransfer toAddress(String toAddress) {
        this.setToAddress(toAddress);
        return this;
    }

    public ItemAssetTransfer isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemAssetTransfer createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemAssetTransfer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemAssetTransfer updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemAssetTransfer updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemAssetTransfer deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemAssetTransfer deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemAssetTransfer company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemAssetTransfer department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemAssetTransfer setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
