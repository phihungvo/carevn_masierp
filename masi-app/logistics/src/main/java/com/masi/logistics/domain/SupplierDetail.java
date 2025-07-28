package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SupplierDetail.
 */
@Data
@Table("supplier_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("supplier_id")
    private UUID supplierId;

    @Column("item_id")
    private UUID itemId;

    @Column("base_price")
    private BigDecimal basePrice;

    @Column("notes")
    private String notes;

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
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SupplierDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public SupplierDetail supplierId(UUID supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public SupplierDetail itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public SupplierDetail basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public SupplierDetail notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public SupplierDetail createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public SupplierDetail createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public SupplierDetail updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public SupplierDetail updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public SupplierDetail deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public SupplierDetail deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public SupplierDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SupplierDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
