package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ReviewStatus;
import com.masi.employee.service.dto.LeaveRequestReviewDTO;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A LeaveRequestReview.
 */
@Table("leave_request_review")
@Data
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequestReview implements Serializable, Persistable<UUID> {

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

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

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
    @JsonIgnoreProperties(value = { "employee", "sensor", "substitute", "reviews" }, allowSetters = true)
    private LeaveRequest leaveRequest;

    @Column("reviewer_id")
    private UUID reviewerId;

    @Column("leave_request_id")
    private UUID leaveRequestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public LeaveRequestReview id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReviewStatus getStatus() {
        return this.status;
    }

    public LeaveRequestReview status(ReviewStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getReason() {
        return this.reason;
    }

    public LeaveRequestReview reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public LeaveRequestReview isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LeaveRequestReview createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public LeaveRequestReview lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Employee getReviewer() {
        return this.reviewer;
    }

    public void setReviewer(Employee reviewer) {
        this.reviewer = reviewer;
        this.reviewerId = reviewer != null ? reviewer.getId() : null;
    }

    public LeaveRequestReview reviewer(Employee reviewer) {
        this.setReviewer(reviewer);
        return this;
    }

    public UUID getReviewerId() {
        return this.reviewerId;
    }

    public void setReviewerId(UUID reviewer) {
        this.reviewerId = reviewer;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public LeaveRequestReview setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public LeaveRequest getLeaveRequest() {
        return this.leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
        this.leaveRequestId = leaveRequest != null ? leaveRequest.getId() : null;
    }

    public LeaveRequestReview leaveRequest(LeaveRequest leaveRequest) {
        this.setLeaveRequest(leaveRequest);
        return this;
    }

    public UUID getLeaveRequestId() {
        return this.leaveRequestId;
    }

    public void setLeaveRequestId(UUID leaveRequest) {
        this.leaveRequestId = leaveRequest;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRequestReview)) {
            return false;
        }
        return getId() != null && getId().equals(((LeaveRequestReview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveRequestReview{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }

    public LeaveRequestReviewDTO toDto() {
        LeaveRequestReviewDTO leaveRequestReviewDTO = new LeaveRequestReviewDTO();
        leaveRequestReviewDTO.setId(this.id);
        leaveRequestReviewDTO.setStatus(this.status);
        leaveRequestReviewDTO.setReason(this.reason);
        leaveRequestReviewDTO.setIsActive(this.isActive);
        leaveRequestReviewDTO.setCreatedAt(this.createdAt);
        leaveRequestReviewDTO.setLastUpdated(this.lastUpdated);
        leaveRequestReviewDTO.setReviewerId(this.reviewerId);
        leaveRequestReviewDTO.setLeaveRequestId(this.leaveRequestId);
        leaveRequestReviewDTO.setLeaveRequest(this.leaveRequest != null ? this.leaveRequest.toDto() : null);
        leaveRequestReviewDTO.setReviewer(this.reviewer != null ? this.reviewer.toDto() : null);
        return leaveRequestReviewDTO;
    }
}
