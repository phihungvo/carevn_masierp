package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.ProductRoutingDTO;
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
 * A ProductRouting.
 */
@Data
@Table("product_routing")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductRouting implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

//    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

//    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

//    @NotNull(message = "must not be null")
    @Column("quantity")
    private Float quantity;

//    @NotNull(message = "must not be null")
    @Column("unit")
    private String unit;

//    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

//    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

//    @NotNull(message = "must not be null")
    @Column("last_updated_at")
    private ZonedDateTime lastUpdatedAt;

    @Transient
    private boolean isPersisted;

    @Transient
    private Factory factory;

    @Transient
    private Storage storage;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("factory_id")
    private UUID factoryId;

    @Column("storage_id")
    private UUID storageId;

    @Column("product_maintain_id")
    private UUID productMaintainId;

    @Transient
    private ProductMaintain productMaintain;

    @Column("warehouse_date")
    private ZonedDateTime warehouseDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public void setUnit(String unit) {
        this.unit = String.valueOf(UUID.randomUUID());
    }


    public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.lastUpdatedAt = ZonedDateTime.now();
    }

    public ProductRouting id(UUID id) {
        this.setId(id);
        return this;
    }

    public ProductRouting name(String name) {
        this.setName(name);
        return this;
    }

    public ProductRouting quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public ProductRouting unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public ProductRouting isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public ProductRouting createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProductRouting lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProductRouting setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public ProductRouting factory(Factory factory) {
        this.setFactory(factory);
        return this;
    }

    public ProductRouting storage(Storage storage) {
        this.setStorage(storage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    public ProductRoutingDTO toDto() {
        ProductRoutingDTO dto = new ProductRoutingDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setQuantity(this.quantity);
        dto.setUnit(this.unit);
        dto.setIsActive(this.isActive);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdatedAt(this.lastUpdatedAt);
        dto.setFactoryId(this.factoryId);
        dto.setStorageId(this.storageId);
        dto.setProductMaintainId(this.productMaintainId);
        //dto.setProductMaintainDTO(this.productMaintain.toDto());
        dto.setWarehouseDate(this.warehouseDate);
        dto.setIsDeleted(this.isDeleted);
        dto.setDeletedAt(this.deletedAt);
        dto.setDeletedBy(this.deletedBy);
        return dto;
    }

}
