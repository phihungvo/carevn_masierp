package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.WorkPlace;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.AnnualLeave} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnualLeaveDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    @Min(value = 1, message = "must be greater than 0")
    private Integer leaveAfterProbation;

    @NotNull(message = "must not be null")
    @Min(value = 1, message = "must be greater than 0")
    private Integer leavePerYear;

    @NotNull(message = "must not be null")
    @Min(value = 1, message = "must be greater than 0")
    @Max(value = 12, message = "must be less than or equal to 12")
    private Integer carryForwardMonth;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private WorkPlace workPlace;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnualLeaveDTO annualLeaveDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, annualLeaveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }


}
