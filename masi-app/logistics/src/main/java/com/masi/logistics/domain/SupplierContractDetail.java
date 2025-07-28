package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
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
 * A SupplierContractDetail.
 */
@Data
@Table("supplier_contract_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContractDetail implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("supply_item_id")
    private UUID supplyItemId;

    @Transient
    private Item item;

    @Column("unit_id")
    private UUID unitId;

    @Transient
    private  Uom unit;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("note")
    private String note;

    @Column("price")
    private BigDecimal price;

    @Column("vat_id")
    private UUID vatId;

    @Column("vat_rate")
    private Double vatRate;

    @Column("vat_amount")
    private BigDecimal vatAmount;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("total_amount_after_vat")
    private BigDecimal totalAmountAfterVat;

    @Column("created_by")
    private String createdBy;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "supplierContractDetails" }, allowSetters = true)
    private SupplierContract supplierContract;

    @Column("supplier_contract_id")
    private UUID supplierContractId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SupplierContractDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public SupplierContractDetail supplyItemId(UUID supplyItemId) {
        this.setSupplyItemId(supplyItemId);
        return this;
    }

    public SupplierContractDetail unitId(UUID unitId) {
        this.setUnitId(unitId);
        return this;
    }

    public SupplierContractDetail quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public SupplierContractDetail note(String note) {
        this.setNote(note);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SupplierContractDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public SupplierContractDetail supplierContract(SupplierContract supplierContract) {
        this.setSupplierContract(supplierContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
