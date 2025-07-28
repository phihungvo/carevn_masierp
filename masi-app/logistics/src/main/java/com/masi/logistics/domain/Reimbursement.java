package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Reimbursement.
 */
@Data
@Table("reimbursement")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reimbursement implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("reimbursement_id")
    private UUID reimbursementId;

    @Column("advance_id")
    private UUID advanceId;

    @Column("invoice_id")
    private UUID invoiceId;

    @Transient
    @JsonIgnoreProperties(value = { "reimbursements" })
    private PaymentRequest advancement;

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

    public Reimbursement id(UUID id) {
        this.setId(id);
        return this;
    }

    public Reimbursement reimbursementId(UUID reimbursementId) {
        this.setReimbursementId(reimbursementId);
        return this;
    }

    public Reimbursement advanceId(UUID advanceId) {
        this.setAdvanceId(advanceId);
        return this;
    }

    public Reimbursement invoiceId(UUID invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public Reimbursement isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Reimbursement createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Reimbursement createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Reimbursement updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Reimbursement updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Reimbursement deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public Reimbursement deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Reimbursement company(String company) {
        this.setCompany(company);
        return this;
    }

    public Reimbursement department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Reimbursement setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
