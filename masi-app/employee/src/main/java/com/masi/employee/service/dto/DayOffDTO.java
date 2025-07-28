package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.DayOff} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DayOffDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float numberDaysOff;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float useDaysOff = 0F;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Float nowDaysOff;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer year;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID employeeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID annualLeave;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    private Boolean isActive;

}
