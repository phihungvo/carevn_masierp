package com.masi.logistics.domain;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.enumeration.ItemType;
import com.masi.logistics.service.dto.ItemTypeDTO;
import io.r2dbc.postgresql.codec.Json;
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
 * A Item.
 */
@Data
@Table("item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("uom_id")
    private UUID uomId;

    @Column("attribute")
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

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
    @JsonIgnoreProperties(value = { "items", "warehouseType" }, allowSetters = true)
    private ItemCategory itemCategory;

    @Transient
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Uom uom;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Column("percent_protein")
    private Float percentProtein ;

    @Column("notes")
    private String notes;

    @Column("vat_rate")
    private Float vatRate;

    @Column("vat_id")
    private UUID vatId;

    @Column("unit_price")
    private Float UnitPrice;

    @Column("revenue_group")
    private UUID revenueGroupId;

    @Transient
    @JsonIgnoreProperties(value = { "items", "warehouseType" }, allowSetters = true)
    private ItemCategory revenueGroup;

    @Column("item_type_id")
    private UUID itemTypeId;

    @Transient
    private com.masi.logistics.domain.ItemType itemTypes;

    @Column("supplier_id")
    private UUID supplierId;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Transient
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private Suppliers supplier;

    @Column("item_type")
    private ItemType itemType;

    @Column("is_active")
    private Boolean isActive;

    @Column("is_separation")
    private Boolean isSeparation;


    public Item id(UUID id) {
        this.setId(id);
        return this;
    }

    public Item code(String code) {
        this.setCode(code);
        return this;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public Item uomId(UUID uomId) {
        this.setUomId(uomId);
        return this;
    }

    public Item attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public Item company(String company) {
        this.setCompany(company);
        return this;
    }

    public Item department(String department) {
        this.setDepartment(department);
        return this;
    }

    public Item isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Item createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Item createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Item updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Item updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Item deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Item deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Item setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public Item itemCategory(ItemCategory itemCategory) {
        this.setItemCategory(itemCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
