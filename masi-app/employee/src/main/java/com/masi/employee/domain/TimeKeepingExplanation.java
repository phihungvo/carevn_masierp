package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import com.masi.employee.service.dto.TimeKeepingExplanationDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TimeKeepingExplanation.
 */
@Data
@Table("time_keeping_explanation")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingExplanation implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("explanation")
    private String explanation;

    @NotNull(message = "must not be null")
    @Column("status")
    private ExplanationStatus status;

    @NotNull(message = "must not be null")
    @Column("reason")
    private TimeKeepingViolationType reason;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    private Employee employee;

    @Transient
    @JsonIgnoreProperties(value = { "timeKeeping", "employee", "explanation" }, allowSetters = true)
    private Set<TimeKeepingViolation> violations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "explanation" }, allowSetters = true)
    private Set<ExplanationReview> reviews = new HashSet<>();

    @Column("employee_id")
    private UUID employeeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TimeKeepingExplanation id(UUID id) {
        this.setId(id);
        return this;
    }

    public TimeKeepingExplanation explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public TimeKeepingExplanation status(ExplanationStatus status) {
        this.setStatus(status);
        return this;
    }

    public TimeKeepingExplanation reason(TimeKeepingViolationType reason) {
        this.setReason(reason);
        return this;
    }

    public TimeKeepingExplanation createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public TimeKeepingExplanation lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public TimeKeepingExplanation isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TimeKeepingExplanation setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public TimeKeepingExplanation employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public TimeKeepingExplanation violations(Set<TimeKeepingViolation> timeKeepingViolations) {
        this.setViolations(timeKeepingViolations);
        return this;
    }

    public TimeKeepingExplanation addViolation(TimeKeepingViolation timeKeepingViolation) {
        this.violations.add(timeKeepingViolation);
        timeKeepingViolation.setExplanation(this);
        return this;
    }

    public TimeKeepingExplanation removeViolation(TimeKeepingViolation timeKeepingViolation) {
        this.violations.remove(timeKeepingViolation);
        timeKeepingViolation.setExplanation(null);
        return this;
    }

    public TimeKeepingExplanation reviews(Set<ExplanationReview> explanationReviews) {
        this.setReviews(explanationReviews);
        return this;
    }

    public TimeKeepingExplanation addReview(ExplanationReview explanationReview) {
        this.reviews.add(explanationReview);
        explanationReview.setExplanation(this);
        return this;
    }

    public TimeKeepingExplanation removeReview(ExplanationReview explanationReview) {
        this.reviews.remove(explanationReview);
        explanationReview.setExplanation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public TimeKeepingExplanationDTO toDto() {
        TimeKeepingExplanationDTO dto = new TimeKeepingExplanationDTO();
        dto.setId(this.getId());
        dto.setExplanation(this.getExplanation());
        dto.setStatus(this.getStatus());
        dto.setReason(this.getReason());
        dto.setIsActive(this.getIsActive());
        dto.setEmployeeId(this.getEmployeeId());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        if (Objects.nonNull(this.getEmployee())) {
            dto.setEmployee(this.getEmployee().toDto());
        }
        if (Objects.nonNull(this.getViolations())) {
            dto.setViolationDtos(this.getViolations().stream().map(TimeKeepingViolation::toDto).collect(Collectors.toSet()));
        }
        if (Objects.nonNull(this.getReviews())) {
            dto.setReviewDtos(this.getReviews().stream().map(ExplanationReview::toDto).collect(Collectors.toSet()));
        }
        return dto;
    }

    public void partialUpdate(TimeKeepingExplanationDTO dto) {
        if ( dto == null ) {
            return;
        }
        if ( dto.getExplanation() != null ) {
            this.setExplanation( dto.getExplanation() );
        }
    }
}
