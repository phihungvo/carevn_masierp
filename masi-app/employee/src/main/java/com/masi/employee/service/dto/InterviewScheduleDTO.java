package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.enumeration.InterviewMode;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.InterviewProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.InterviewSchedule} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterviewScheduleDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    private String candidateName;

    @NotNull(message = "must not be null")
    private ZonedDateTime interviewDate;

    private String cvFile;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private FileAttachmentDTO cvFileAttachment;

    @NotNull(message = "must not be null")
    private UUID interviewerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InterviewProcess process;

    private InterviewMode interviewMode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InterviewResult interviewResult;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = RecruitmentRequestDTO.class)
    private RecruitmentRequestDTO recruitmentRequest;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = EmployeeDTO.class)
    private EmployeeDTO interviewer;

    private UUID recruitmentRequestId;

    private String email;

    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterviewScheduleDTO interviewScheduleDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interviewScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
