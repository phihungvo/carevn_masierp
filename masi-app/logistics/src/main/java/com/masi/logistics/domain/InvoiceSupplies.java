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
 * A InvoiceSupplies.
 */
@Data
@Table("invoice_supplies")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceSupplies implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("item_id")
    private UUID itemId;

    @Column("detail_1")
    private String detail1;

    @Column("detail_2")
    private String detail2;

    @Column("invoice_id")
    private UUID invoiceId;

    @Column("supply_id")
    private UUID supplyId;

    @Column("quantity")
    private BigDecimal quantity;

    @Column("price")
    private BigDecimal price;

    @Column("total")
    private BigDecimal total;

    @Column("vat")
    private BigDecimal vat;

    @Column("description")
    private String description;

    @Column("vat_amount")
    private BigDecimal vatAmount; // VAT amount

    @Column("pre_import_fee")
    private BigDecimal preImportFee; // Pre-import fee

    @Column("import_vat_percentage")
    private BigDecimal importVatPercentage; // Import vat percentage

    @Column("import_tax_percentage")
    private BigDecimal importTaxPercentage; // Import tax percentage

    @Column("import_tax_amount")
    private BigDecimal importTaxAmount; // Import tax amount

    @Column("import_vat_amount")
    private BigDecimal importVatAmount; // Import vat amount

    @Column("env_fee_percentage")
    private BigDecimal envFeePercentage; // Environmental fee percentage

    @Column("env_fee_amount")
    private BigDecimal envFeeAmount; // Environmental fee amount

    @Column("post_import_fee")
    private BigDecimal postImportFee; // Post-import fee

    @Column("grand_total")
    private BigDecimal grandTotal; // Grand total

    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
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

    @Column("note")
    private String note;

    @Column("vat_id")
    private UUID vatId;

    @Column("total_amount_import_stock")
    private BigDecimal totalAmountImportStock;

    @Transient
    private Item item;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public InvoiceSupplies id(UUID id) {
        this.setId(id);
        return this;
    }

    public InvoiceSupplies itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public InvoiceSupplies detail1(String detail1) {
        this.setDetail1(detail1);
        return this;
    }

    public InvoiceSupplies detail2(String detail2) {
        this.setDetail2(detail2);
        return this;
    }

    public InvoiceSupplies invoiceId(UUID invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public InvoiceSupplies supplyId(UUID supplyId) {
        this.setSupplyId(supplyId);
        return this;
    }

    public InvoiceSupplies quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public InvoiceSupplies price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public InvoiceSupplies total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public InvoiceSupplies tax(BigDecimal vat) {
        this.setVat(vat);
        return this;
    }

    public InvoiceSupplies description(String description) {
        this.setDescription(description);
        return this;
    }

    public InvoiceSupplies isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InvoiceSupplies createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InvoiceSupplies createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InvoiceSupplies updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InvoiceSupplies updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InvoiceSupplies deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InvoiceSupplies deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InvoiceSupplies company(String company) {
        this.setCompany(company);
        return this;
    }

    public InvoiceSupplies department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InvoiceSupplies setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
