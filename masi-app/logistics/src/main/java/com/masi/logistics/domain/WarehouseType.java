package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WarehouseType.
 */
@Data
@Table("warehouse_type")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseType implements Serializable, Persistable<UUID> {

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

    @Column("active")
    private Boolean active;

    @Column("use_manufacture")
    private Boolean useManufacture;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "inventories", "warehouseType" }, allowSetters = true)
    private Set<Warehouse> warehouses = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "warehouseType" }, allowSetters = true)
    private Set<WarehouseRole> warehouseRoles = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "items", "warehouseType" }, allowSetters = true)
    private Set<ItemCategory> itemCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public WarehouseType id(UUID id) {
        this.setId(id);
        return this;
    }

    public WarehouseType code(String code) {
        this.setCode(code);
        return this;
    }

    public WarehouseType name(String name) {
        this.setName(name);
        return this;
    }

    public WarehouseType description(String description) {
        this.setDescription(description);
        return this;
    }

    public WarehouseType active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public WarehouseType createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public WarehouseType createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public WarehouseType updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public WarehouseType updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public WarehouseType deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public WarehouseType deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public WarehouseType company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public WarehouseType setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public WarehouseType warehouses(Set<Warehouse> warehouses) {
        this.setWarehouses(warehouses);
        return this;
    }

    public WarehouseType addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
        warehouse.setWarehouseType(this);
        return this;
    }

    public WarehouseType removeWarehouse(Warehouse warehouse) {
        this.warehouses.remove(warehouse);
        warehouse.setWarehouseType(null);
        return this;
    }

    public WarehouseType warehouseRoles(Set<WarehouseRole> warehouseRoles) {
        this.setWarehouseRoles(warehouseRoles);
        return this;
    }

    public WarehouseType addWarehouseRole(WarehouseRole warehouseRole) {
        this.warehouseRoles.add(warehouseRole);
        warehouseRole.setWarehouseType(this);
        return this;
    }

    public WarehouseType removeWarehouseRole(WarehouseRole warehouseRole) {
        this.warehouseRoles.remove(warehouseRole);
        warehouseRole.setWarehouseType(null);
        return this;
    }

    public WarehouseType itemCategories(Set<ItemCategory> itemCategories) {
        this.setItemCategories(itemCategories);
        return this;
    }

    public WarehouseType addItemCategory(ItemCategory itemCategory) {
        this.itemCategories.add(itemCategory);
        itemCategory.setWarehouseType(this);
        return this;
    }

    public WarehouseType removeItemCategory(ItemCategory itemCategory) {
        this.itemCategories.remove(itemCategory);
        itemCategory.setWarehouseType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
