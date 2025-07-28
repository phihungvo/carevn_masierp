package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.WarehouseTypePage;
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
 * A Warehouse.
 */
@Data
@Table("warehouse")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Warehouse implements Serializable, Persistable<UUID> {
    public static final String ENTITY_NAME = "masiLogisticsWarehouse";

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("active")
    private Boolean active;

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

    @Column("warehouse_type_page")
    private WarehouseTypePage warehouseTypePage;

    @Transient
    @JsonIgnoreProperties(value = { "warehouses", "warehouseRoles", "itemCategories" }, allowSetters = true)
    private WarehouseType warehouseType = new WarehouseType();

    @Column("warehouse_type_id")
    private UUID warehouseTypeId = UUID.randomUUID();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Warehouse id(UUID id) {
        this.setId(id);
        return this;
    }

    public Warehouse code(String code) {
        this.setCode(code);
        return this;
    }

    public Warehouse name(String name) {
        this.setName(name);
        return this;
    }

    public Warehouse address(String address) {
        this.setAddress(address);
        return this;
    }

    public Warehouse active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public Warehouse createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public Warehouse createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public Warehouse updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public Warehouse updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public Warehouse deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public Warehouse deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public Warehouse company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Warehouse setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public Warehouse warehouseType(WarehouseType warehouseType) {
        this.setWarehouseType(warehouseType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
