package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.QuotationStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Quotation.
 */
@Data
@Table("quotation")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quotation implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("status")
    private QuotationStatus status;

    @NotNull(message = "must not be null")
    @Column("description")
    private String description;

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

    @Column("reject_note")
    private String rejectNote;

    @Column("approval_sign_file")
    private String approvalSignFile;
    @Column("approval_sign_name")
    private String approvalSignName;

    @Column("payment_method")
    private String paymentMethod;

    @Column("payment_method_en")
    private String paymentMethodEn;

    @Column("delivery_location")
    private String deliveryLocation;

    @Column("price_type")
    private String priceType;

    @Column("price_type_en")
    private String priceTypeEn;

    @Column("material_criteria")
    private String materialCriteria;

    @Column("material_criteria_en")
    private String materialCriteriaEn;

    @Column("delivery_location_en")
    private String deliveryLocationEn;

    @Column("delivery_date")
    private ZonedDateTime deliveryDate;

    @Column("packaging")
    private String packaging;

    @Column("packaging_en")
    private String packagingEn;

    @Column("minimum_weight")
    private String minimumWeight;

    @Column("approver_id")
    private UUID approverId;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("customer_reject_note")
    private String customerRejectNote;

    @Column("customer_approver_id")
    private UUID customerApproverId;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("updated_by")
    private String updatedBy;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_date")
    private ZonedDateTime deletedDate;

    @Column("deleted_by")
    private String deletedBy;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "quotation" }, allowSetters = true)
    private Set<QuotationExport> quotationExports = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "quotation" }, allowSetters = true)
    private Set<QuotationDetail> quotationDetails = new HashSet<>();

    @Column("customer_id")
    private UUID customerId;

    @Transient
    @JsonIgnoreProperties(value = { "quotation" }, allowSetters = true)
    private Customer customer;

    @Column("process_at")
    private ZonedDateTime processAt;



    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Quotation id(UUID id) {
        this.setId(id);
        return this;
    }

    public Quotation name(String name) {
        this.setName(name);
        return this;
    }

    public Quotation status(QuotationStatus status) {
        this.setStatus(status);
        return this;
    }

    public Quotation description(String description) {
        this.setDescription(description);
        return this;
    }

    public Quotation fileId(String fileId) {
        this.setFileId(fileId);
        return this;
    }

    public Quotation fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public Quotation rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }

    public Quotation approverId(UUID approverId) {
        this.setApproverId(approverId);
        return this;
    }

    public Quotation company(String company) {
        this.setCompany(company);
        return this;
    }

    public Quotation lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public Quotation updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Quotation createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Quotation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Quotation isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Quotation deletedDate(ZonedDateTime deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public Quotation deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }



    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Quotation setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Quotation quotationExports(Set<QuotationExport> quotationExports) {
        this.setQuotationExports(quotationExports);
        return this;
    }

    public Quotation addQuotationExport(QuotationExport quotationExport) {
        this.quotationExports.add(quotationExport);
        quotationExport.setQuotation(this);
        return this;
    }

    public Quotation removeQuotationExport(QuotationExport quotationExport) {
        this.quotationExports.remove(quotationExport);
        quotationExport.setQuotation(null);
        return this;
    }

    public Quotation quotationDetails(Set<QuotationDetail> quotationDetails) {
        this.setQuotationDetails(quotationDetails);
        return this;
    }

    public Quotation addQuotationDetail(QuotationDetail quotationDetail) {
        this.quotationDetails.add(quotationDetail);
        quotationDetail.setQuotation(this);
        return this;
    }

    public Quotation removeQuotationDetail(QuotationDetail quotationDetail) {
        this.quotationDetails.remove(quotationDetail);
        quotationDetail.setQuotation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
