package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.PurchaseReviewStatus;
import jakarta.validation.constraints.*;

import java.io.Serial;
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
 * A PurchaseReview.
 */
@Data
@Table("purchase_review")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseReview implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private PurchaseReviewStatus status;

    @Column("approval_status_sign_file")
    private String approvalStatusSignFile;

    @Column("approval_status_note")
    private String approvalStatusNote;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "purchaseRequestFiles", "purchaseDeliveries", "purchaseReviews" }, allowSetters = true)
    private PurchaseRequest purchaseRequest;

    @Column("purchase_request_id")
    private UUID purchaseRequestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public PurchaseReview id(UUID id) {
        this.setId(id);
        return this;
    }

    public PurchaseReview status(PurchaseReviewStatus status) {
        this.setStatus(status);
        return this;
    }



    public PurchaseReview approvalStatusNote(String approvalStatusNote) {
        this.setApprovalStatusNote(approvalStatusNote);
        return this;
    }

    public PurchaseReview lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public PurchaseReview createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public PurchaseReview employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PurchaseReview setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public PurchaseReview purchaseRequest(PurchaseRequest purchaseRequest) {
        this.setPurchaseRequest(purchaseRequest);
        return this;
    }
    public PurchaseReview purchaseRequestId(UUID purchaseRequest) {
        this.setPurchaseRequestId(purchaseRequest);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
