package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.SampleDisposalDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SampleDisposal.
 */
@Data
@EqualsAndHashCode
@ToString
@Table("sample_disposal")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SampleDisposal implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

//    @NotNull(message = "must not be null")
    @Column("request_date")
    private LocalDate requestDate;

//    @NotNull(message = "must not be null")
    @Column("involve_employee")
    private String involveEmployee;

//    @NotNull(message = "must not be null")
    @Column("position")
    private String position;

//    @NotNull(message = "must not be null")
    @Column("disposal_note")
    private String disposalNote;

//    @NotNull(message = "must not be null")
    @Column("quantity_stt")
    private Integer quantityStt;

//    @NotNull(message = "must not be null")
    @Column("quantity_sample_name")
    private String quantitySampleName;

//    @NotNull(message = "must not be null")
    @Column("quantity_sample_no")
    private String quantitySampleNo;

//    @NotNull(message = "must not be null")
    @Column("quantity")
    private Float quantity;

//    @NotNull(message = "must not be null")
    @Column("quantity_save_date")
    private LocalDate quantitySaveDate;

//    @NotNull(message = "must not be null")
    @Column("quantity_release_date")
    private LocalDate quantityReleaseDate;

//    @NotNull(message = "must not be null")
    @Column("disposal_method")
    private String disposalMethod;

//    @NotNull(message = "must not be null")
    @Column("disposal_result")
    private String disposalResult;

//    @NotNull(message = "must not be null")
    @Column("reviewer_id")
    private UUID reviewerId;

//    @NotNull(message = "must not be null")
    @Column("requester_id")
    private UUID requesterId;

    @Column("reviewer_approved")
    private Boolean reviewerApproved;

    @Column("reviewer_note")
    private String reviewerNote;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("reviewer_sign_file")
    private String reviewerSignFile;


//    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Transient
    private boolean isPersisted;

    @Transient
    private QualityCheckSample sample;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SampleDisposal id(UUID id) {
        this.setId(id);
        return this;
    }

    public SampleDisposal requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public SampleDisposal involveEmployee(String involveEmployee) {
        this.setInvolveEmployee(involveEmployee);
        return this;
    }

    public SampleDisposal position(String position) {
        this.setPosition(position);
        return this;
    }

    public SampleDisposal disposalNote(String disposalNote) {
        this.setDisposalNote(disposalNote);
        return this;
    }

    public SampleDisposal quantityStt(Integer quantityStt) {
        this.setQuantityStt(quantityStt);
        return this;
    }

    public SampleDisposal quantitySampleName(String quantitySampleName) {
        this.setQuantitySampleName(quantitySampleName);
        return this;
    }

    public SampleDisposal quantitySampleNo(String quantitySampleNo) {
        this.setQuantitySampleNo(quantitySampleNo);
        return this;
    }

    public SampleDisposal quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public SampleDisposal quantitySaveDate(LocalDate quantitySaveDate) {
        this.setQuantitySaveDate(quantitySaveDate);
        return this;
    }

    public SampleDisposal quantityReleaseDate(LocalDate quantityReleaseDate) {
        this.setQuantityReleaseDate(quantityReleaseDate);
        return this;
    }

    public SampleDisposal disposalMethod(String disposalMethod) {
        this.setDisposalMethod(disposalMethod);
        return this;
    }

    public SampleDisposal disposalResult(String disposalResult) {
        this.setDisposalResult(disposalResult);
        return this;
    }

    public SampleDisposal reviewerId(UUID reviewerId) {
        this.setReviewerId(reviewerId);
        return this;
    }

    public SampleDisposal requesterId(UUID requesterId) {
        this.setRequesterId(requesterId);
        return this;
    }

    public SampleDisposal reviewerApproved(Boolean reviewerApproved) {
        this.setReviewerApproved(reviewerApproved);
        return this;
    }

    public SampleDisposal reviewerNote(String reviewerNote) {
        this.setReviewerNote(reviewerNote);
        return this;
    }

    public SampleDisposal createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public SampleDisposal lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }





    public SampleDisposal isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SampleDisposal setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public SampleDisposal sample(QualityCheckSample qualityCheckSample) {
        this.setSample(qualityCheckSample);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public SampleDisposalDTO toDto() {
        SampleDisposalDTO dto = new SampleDisposalDTO();
        dto.setId(this.getId());
        dto.setRequestDate(this.requestDate);
        if (Objects.nonNull(this.requestDate)) dto.setZonedRequestDate(this.requestDate.atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setInvolveEmployee(this.getInvolveEmployee());
        dto.setPosition(this.getPosition());
        dto.setDisposalNote(this.getDisposalNote());
        dto.setQuantityStt(this.getQuantityStt());
        dto.setQuantitySampleName(this.getQuantitySampleName());
        dto.setQuantitySampleNo(this.getQuantitySampleNo());
        dto.setQuantity(this.getQuantity());
        dto.setQuantitySaveDate(this.getQuantitySaveDate());
        if (Objects.nonNull(this.getQuantitySaveDate())) dto.setZonedQuantitySaveDate(this.getQuantitySaveDate().atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setQuantityReleaseDate(this.getQuantityReleaseDate());
        if (Objects.nonNull(this.getQuantityReleaseDate())) dto.setZonedQuantityReleaseDate(this.getQuantityReleaseDate().atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setDisposalMethod(this.getDisposalMethod());
        dto.setDisposalResult(this.getDisposalResult());
        dto.setReviewerId(this.getReviewerId());
        dto.setRequesterId(this.getRequesterId());
        dto.setReviewerApproved(this.getReviewerApproved());
        dto.setReviewerNote(this.getReviewerNote());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setReviewerSignFile(this.getReviewerSignFile());
        dto.setIsActive(this.getIsActive());
        return dto;
    }
}
