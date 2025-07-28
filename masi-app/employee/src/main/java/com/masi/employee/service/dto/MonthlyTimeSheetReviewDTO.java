package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.MonthlyTimeSheetReview} entity.
 */
@Data
public class MonthlyTimeSheetReviewDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private TimesheetReviewStatus status;

    private String note;

    private String signatureFile;

    private FileAttachmentDTO signature;


    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthlyTimeSheetReviewDTO)) {
            return false;
        }

        MonthlyTimeSheetReviewDTO monthlyTimeSheetReviewDTO = (MonthlyTimeSheetReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monthlyTimeSheetReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

}
