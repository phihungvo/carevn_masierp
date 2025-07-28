package com.masi.employee.service.dto;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.domain.enumeration.ReviewStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.ExplanationReview} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExplanationReviewDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private ReviewStatus status;

    private String reason;

    private Boolean isActive;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private EmployeeDTO reviewer;

    private TimeKeepingExplanationDTO explanation;

    private UUID reviewerId;

    private UUID explanationId;

    public ExplanationReviewDTO(UUID reviewerId, UUID explanationId) {
        this.reviewerId = reviewerId;
        this.explanationId = explanationId;
        this.isActive = true;
        this.status = ReviewStatus.AWAITING_REVIEW;
        this.createdAt = ZonedDateTime.now();
        this.lastUpdated = this.createdAt;
    }

    public ExplanationReviewDTO() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExplanationReviewDTO)) {
            return false;
        }

        ExplanationReviewDTO explanationReviewDTO = (ExplanationReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, explanationReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExplanationReviewDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", reviewer=" + getReviewer() +
            ", explanation=" + getExplanation() +
            "}";
    }
}
