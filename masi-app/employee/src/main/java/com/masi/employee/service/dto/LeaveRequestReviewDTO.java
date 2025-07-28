package com.masi.employee.service.dto;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.LeaveRequestReview;
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
 * A DTO for the {@link com.masi.employee.domain.LeaveRequestReview} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequestReviewDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private ReviewStatus status;

    private String reason;

    private Boolean isActive;

    private UUID reviewerId;

    private UUID leaveRequestId;

    private String fileId;

    private String fileName;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private LeaveRequestDTO leaveRequest;

    private EmployeeDTO reviewer;

    public LeaveRequestReviewDTO(UUID reviewerId, UUID leaveRequestId) {
        this.reviewerId = reviewerId;
        this.leaveRequestId = leaveRequestId;
        this.isActive = true;
        this.status = ReviewStatus.AWAITING_REVIEW;
        this.createdAt = ZonedDateTime.now();
        this.lastUpdated = this.createdAt;
    }

    public LeaveRequestReviewDTO() {
        // Empty constructor needed for Jackson.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRequestReviewDTO)) {
            return false;
        }

        LeaveRequestReviewDTO leaveRequestReviewDTO = (LeaveRequestReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveRequestReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveRequestReviewDTO{" +
                "id='" + getId() + "'" +
                ", status='" + getStatus() + "'" +
                ", reason='" + getReason() + "'" +
                ", isActive='" + getIsActive() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", lastUpdated='" + getLastUpdated() + "'" +
                ", leaveRequest=" + getLeaveRequest() +
                "}";
    }

    public LeaveRequestReview toEntity() {
        LeaveRequestReview leaveRequestReview = new LeaveRequestReview();
        leaveRequestReview.setId(this.id);
        leaveRequestReview.setStatus(this.status);
        leaveRequestReview.setReason(this.reason);
        leaveRequestReview.setIsActive(this.isActive);
        leaveRequestReview.setLeaveRequestId(this.leaveRequestId);
        leaveRequestReview.setCreatedAt(this.createdAt);
        leaveRequestReview.setLastUpdated(this.lastUpdated);
        leaveRequestReview.setFileId(this.fileId);
        leaveRequestReview.setFileName(this.fileName);
        return leaveRequestReview;
    }
}
