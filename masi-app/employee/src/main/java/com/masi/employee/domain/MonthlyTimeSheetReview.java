package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.service.dto.MonthlyTimeSheetReviewDTO;
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
 * A MonthlyTimeSheetReview.
 */
@Data
@Table("monthly_time_sheet_review")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonthlyTimeSheetReview implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private TimesheetReviewStatus status;

    @Column("note")
    private String note;

    @Column("signature_file")
    private String signatureFile;


    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MonthlyTimeSheetReview id(UUID id) {
        this.setId(id);
        return this;
    }

    public MonthlyTimeSheetReview status(TimesheetReviewStatus status) {
        this.setStatus(status);
        return this;
    }

    public MonthlyTimeSheetReview note(String note) {
        this.setNote(note);
        return this;
    }


    public MonthlyTimeSheetReview createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public MonthlyTimeSheetReview lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MonthlyTimeSheetReview setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public MonthlyTimeSheetReviewDTO toDto() {
        var dto = new MonthlyTimeSheetReviewDTO();
        dto.setId(this.getId());
        dto.setStatus(this.getStatus());
        dto.setNote(this.getNote());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setSignatureFile(this.getSignatureFile());
        return dto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
