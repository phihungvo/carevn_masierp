package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.masi.logistics.domain.enumeration.ItemTypeCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemCategory.
 */
@Data
@Table("item_category")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCategory implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "inventories", "itemCategory" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "warehouses", "warehouseRoles", "itemCategories" }, allowSetters = true)
    private WarehouseType warehouseType;

    @Column("warehouse_type_id")
    private UUID warehouseTypeId;

    @Column("type_item_category")
    private ItemTypeCategory itemTypeCategory;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ItemCategory id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemCategory name(String name) {
        this.setName(name);
        return this;
    }

    public ItemCategory company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemCategory department(String department) {
        this.setDepartment(department);
        return this;
    }

    public ItemCategory isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemCategory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemCategory createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public ItemCategory updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemCategory updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemCategory deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemCategory deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemCategory setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public ItemCategory items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public ItemCategory addItem(Item item) {
        this.items.add(item);
        item.setItemCategory(this);
        return this;
    }

    public ItemCategory removeItem(Item item) {
        this.items.remove(item);
        item.setItemCategory(null);
        return this;
    }

    public ItemCategory warehouseType(WarehouseType warehouseType) {
        this.setWarehouseType(warehouseType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
