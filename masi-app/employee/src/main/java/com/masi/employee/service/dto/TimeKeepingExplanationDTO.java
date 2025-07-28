package com.masi.employee.service.dto;

import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.TimeKeepingExplanation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingExplanationDTO implements Serializable {

    private UUID id;

    private String explanation;

    private ExplanationStatus status;

    private TimeKeepingViolationType reason;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private Boolean isActive;

    private EmployeeDTO employee;

    private UUID employeeId;

    private Set<TimeKeepingViolation> violations;

    private Set<TimeKeepingViolationDTO> violationDtos;

    private LocalDate fromDate;

    private LocalDate toDate;
    private String company;

    private Set<UUID> violationIds;
    private Set<UUID> reviewerIds = new HashSet<>();

    private Set<ExplanationReviewDTO> reviewDtos;

    public void addReview(ExplanationReviewDTO review) {
        if (this.reviewDtos == null) {
            this.reviewDtos = new HashSet<>();
        }
        this.reviewDtos.add(review);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeKeepingExplanationDTO)) {
            return false;
        }

        TimeKeepingExplanationDTO timeKeepingExplanationDTO = (TimeKeepingExplanationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeKeepingExplanationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeKeepingExplanationDTO{" +
            "id='" + getId() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
