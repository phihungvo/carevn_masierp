package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ReviewStatus;
import com.masi.employee.service.dto.ExplanationReviewDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ExplanationReview.
 */
@Table("explanation_review")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExplanationReview implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private ReviewStatus status;

    @Column("reason")
    private String reason;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Transient
    private boolean isPersisted;

    @Transient
    private Employee reviewer;

    @Transient
    @JsonIgnoreProperties(value = { "violations" }, allowSetters = true)
    private TimeKeepingExplanation explanation;

    @Column("reviewer_id")
    private UUID reviewerId;

    @Column("explanation_id")
    private UUID explanationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ExplanationReview id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReviewStatus getStatus() {
        return this.status;
    }

    public ExplanationReview status(ReviewStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getReason() {
        return this.reason;
    }

    public ExplanationReview reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ExplanationReview isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ExplanationReview createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public ExplanationReview lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ExplanationReview setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Employee getReviewer() {
        return this.reviewer;
    }

    public void setReviewer(Employee employee) {
        this.reviewer = employee;
        this.reviewerId = employee != null ? employee.getId() : null;
    }

    public ExplanationReview reviewer(Employee employee) {
        this.setReviewer(employee);
        return this;
    }

    public TimeKeepingExplanation getExplanation() {
        return this.explanation;
    }

    public void setExplanation(TimeKeepingExplanation timeKeepingExplanation) {
        this.explanation = timeKeepingExplanation;
        this.explanationId = timeKeepingExplanation != null ? timeKeepingExplanation.getId() : null;
    }

    public ExplanationReview explanation(TimeKeepingExplanation timeKeepingExplanation) {
        this.setExplanation(timeKeepingExplanation);
        return this;
    }

    public UUID getReviewerId() {
        return this.reviewerId;
    }

    public void setReviewerId(UUID employee) {
        this.reviewerId = employee;
    }

    public UUID getExplanationId() {
        return this.explanationId;
    }

    public void setExplanationId(UUID timeKeepingExplanation) {
        this.explanationId = timeKeepingExplanation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExplanationReview)) {
            return false;
        }
        return getId() != null && getId().equals(((ExplanationReview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExplanationReview{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }

    public ExplanationReviewDTO toDto() {
        ExplanationReviewDTO dto = new ExplanationReviewDTO();
        dto.setId(this.getId());
        dto.setStatus(this.getStatus());
        dto.setReason(this.getReason());
        dto.setIsActive(this.getIsActive());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setReviewerId(this.getReviewerId());
        dto.setExplanationId(this.getExplanationId());
        return dto;
    }
}
