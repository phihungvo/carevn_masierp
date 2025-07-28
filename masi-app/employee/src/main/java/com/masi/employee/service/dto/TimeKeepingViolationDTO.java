package com.masi.employee.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * A DTO for the {@link com.masi.employee.domain.TimeKeepingViolation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeKeepingViolationDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private TimeKeepingViolationType type;

    private Boolean isActive;

    private TimeKeepingDTO timeKeeping;

    private EmployeeDTO employee;

    private TimeKeepingExplanationDTO explanation;

    @NotNull(message = "must not be null")
    private UUID timeKeepingId;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    private UUID explanationId;


    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdatedAt;

    public TimeKeepingViolation toEntity() {
        TimeKeepingViolation entity = new TimeKeepingViolation();
        entity.setId(this.id);
        entity.setType(this.type);
        entity.setTimeKeepingId(this.timeKeepingId);
        return entity;
    }
}
