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
 * A SuppliesItem.
 */
@Data
@Table("supplies_item")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesItem implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("id_supplies_request")
    private UUID idSuppliesRequest;

    @NotNull(message = "must not be null")
    @Column("id_item")
    private UUID idItem;

    @Transient
    private Item item;

    @Column("id_uom")
    private UUID idUom;

    @Column("supplies_id")
    private UUID suppliesId;

    @Transient
    private Suppliers suppliers;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private BigDecimal quantity;

    @Column("price")
    private BigDecimal price;

    @Column("image_ids")
    private String imageIds;

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

    @Column("bank_info")
    private String bankInfo;

    @Column("note")
    private String note;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("vat")
    private Double vat;

    @Column("vat_id")
    private UUID vatId;

    @Column("total_amount_after_vat")
    private BigDecimal totalAmountAfterVat;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SuppliesItem id(UUID id) {
        this.setId(id);
        return this;
    }

    public SuppliesItem idSuppliesRequest(UUID idSuppliesRequest) {
        this.setIdSuppliesRequest(idSuppliesRequest);
        return this;
    }

    public SuppliesItem idItem(UUID idItem) {
        this.setIdItem(idItem);
        return this;
    }

    public SuppliesItem idUom(UUID idUom) {
        this.setIdUom(idUom);
        return this;
    }

    public SuppliesItem suppliesId(UUID suppliesId) {
        this.setSuppliesId(suppliesId);
        return this;
    }

    public SuppliesItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public SuppliesItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public SuppliesItem company(String company) {
        this.setCompany(company);
        return this;
    }

    public SuppliesItem department(String department) {
        this.setDepartment(department);
        return this;
    }

    public SuppliesItem isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public SuppliesItem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public SuppliesItem createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public SuppliesItem updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public SuppliesItem updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public SuppliesItem deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public SuppliesItem deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SuppliesItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
