package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.domain.enumeration.ItemType;
import com.masi.logistics.service.InventoriesService;
import com.masi.logistics.service.dto.InventoriesStorageDTO;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.apache.poi.hpsf.Decimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A InventoriesStorage.
 */
@Data
@Table("inventories_storage")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesStorage implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("item_id")
    private UUID itemId;


    @Transient
    @JsonIgnoreProperties(value = { "inventoriesStorage"}, allowSetters = true)
    private Item item;

    @Column("inventories_detail_id")
    private UUID inventoriesDetailId;
    @Transient
    private InventoriesDetail inventoriesDetail;

    @Column("import_date")
    private ZonedDateTime importDate;

    @Column("export_date")
    private ZonedDateTime exportDate;

    @Column("depreciation")
    private String depreciation;

    @Column("item_type")
    private ItemType itemType;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("price")
    private BigDecimal price;

    @Column("remaining_price")
    private BigDecimal remainingPrice;

    @Column("status")
    private ItemStatus status;

    @Column("expiry_date")
    private ZonedDateTime expiryDate;

    @Column("notes")
    private String notes;

    @Column("attribute")
    private Json attribute;

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

    @Column("warehouse_id")
    private UUID warehouseId;

    @Transient
    private Warehouse warehouse;

    @Column("asset_logs")
    private Json assetLogs;

    @Column("supplier_id")
    private UUID supplierId;

    @Transient
    private Suppliers supplier;

//    @Column("item_category_id")
//    private UUID itemCategoryId;
//
//    @Column("item_sup_category_id")
//    private UUID itemSupCategoryId;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InventoriesStorage id(UUID id) {
        this.setId(id);
        return this;
    }

    public InventoriesStorage code(String code) {
        this.setCode(code);
        return this;
    }

    public InventoriesStorage itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public InventoriesStorage inventoriesDetailId(UUID inventoriesDetailId) {
        this.setInventoriesDetailId(inventoriesDetailId);
        return this;
    }

    public InventoriesStorage importDate(ZonedDateTime importDate) {
        this.setImportDate(importDate);
        return this;
    }

    public InventoriesStorage exportDate(ZonedDateTime exportDate) {
        this.setExportDate(exportDate);
        return this;
    }

    public InventoriesStorage depreciation(String depreciation) {
        this.setDepreciation(depreciation);
        return this;
    }

    public InventoriesStorage expiryDate(ZonedDateTime expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public InventoriesStorage notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public InventoriesStorage attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public InventoriesStorage isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InventoriesStorage createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InventoriesStorage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InventoriesStorage updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InventoriesStorage updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InventoriesStorage deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InventoriesStorage deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InventoriesStorage company(String company) {
        this.setCompany(company);
        return this;
    }

    public InventoriesStorage department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InventoriesStorage setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public InventoriesStorageDTO toDto() {
        InventoriesStorageDTO dto = new InventoriesStorageDTO();
        dto.setId(this.id);
        dto.setCode(this.code);
        dto.setItemId(this.itemId);
        dto.setInventoriesDetailId(this.inventoriesDetailId);
        dto.setImportDate(this.importDate);
        dto.setExportDate(this.exportDate);
        dto.setDepreciation(this.depreciation);
        dto.setExpiryDate(this.expiryDate);
        dto.setNotes(this.notes);
        dto.setAttribute(this.attribute);
        dto.setIsDeleted(this.isDeleted);
        dto.setCreatedAt(this.createdAt);
        dto.setCreatedBy(this.createdBy);
        dto.setUpdatedAt(this.updatedAt);
        dto.setUpdatedBy(this.updatedBy);
        dto.setDeletedAt(this.deletedAt);
        dto.setDeletedBy(this.deletedBy);
        dto.setCompany(this.company);
        dto.setDepartment(this.department);
        return dto;
    }
    public record Transaction(TransactionEnum type, ZonedDateTime date, String code, String costInfo, BigDecimal quantity, BigDecimal basePrice, String deprecation, String monthDeprecation, String description){}
    public record AssetLog(List<Transaction> transactions){}
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    public enum TransactionEnum{
        ADD,
        UPDATE,
        LIQUIDATION,
        DEPRECIATION,
        TRANSFER
    }
}
