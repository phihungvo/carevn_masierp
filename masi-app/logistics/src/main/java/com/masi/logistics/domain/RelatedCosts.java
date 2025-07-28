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
 * A RelatedCosts.
 */
@Data
@Table("related_costs")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RelatedCosts implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("invoice_id")
    private UUID invoiceId;

    @Transient
    @JsonIgnoreProperties(value = { "relatedCosts" }, allowSetters = true)
    private IncomingInvoice invoice;

    @Column("payment_method_id")
    private UUID paymentMethodId;

    @Column("payment_method_code")
    private String paymentMethodCode;

    @Column("payment_method_name")
    private String paymentMethodName;

    @Column("vat_id")
    private UUID vatId;

    @Column("vat")
    private Double vat;

    @Column("vat_amount")
    private BigDecimal vatAmount;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("debt_days")
    private Double debtDays;

    @Column("note")
    private String note;

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

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RelatedCosts id(UUID id) {
        this.setId(id);
        return this;
    }

    public RelatedCosts invoiceId(UUID invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public RelatedCosts paymentMethodId(UUID paymentMethodId) {
        this.setPaymentMethodId(paymentMethodId);
        return this;
    }

    public RelatedCosts paymentMethodCode(String paymentMethodCode) {
        this.setPaymentMethodCode(paymentMethodCode);
        return this;
    }

    public RelatedCosts paymentMethodName(String paymentMethodName) {
        this.setPaymentMethodName(paymentMethodName);
        return this;
    }

    public RelatedCosts vatId(UUID vatId) {
        this.setVatId(vatId);
        return this;
    }

    public RelatedCosts vat(Double vat) {
        this.setVat(vat);
        return this;
    }

    public RelatedCosts vatAmount(BigDecimal vatAmount) {
        this.setVatAmount(vatAmount);
        return this;
    }

    public RelatedCosts totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public RelatedCosts debtDays(Double debtDays) {
        this.setDebtDays(debtDays);
        return this;
    }

    public RelatedCosts note(String note) {
        this.setNote(note);
        return this;
    }

    public RelatedCosts isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public RelatedCosts createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public RelatedCosts createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public RelatedCosts updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public RelatedCosts updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public RelatedCosts deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public RelatedCosts deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public RelatedCosts company(String company) {
        this.setCompany(company);
        return this;
    }

    public RelatedCosts department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public RelatedCosts setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
