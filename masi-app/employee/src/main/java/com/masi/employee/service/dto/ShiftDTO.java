package com.masi.employee.service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.Shift} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShiftDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private UUID idStandardWorkScheduleConfig;

    private String shiftName;

    private Float durationHours;

    private Integer hourStartTime;

    private Integer minuteStartTime;

    private Integer secondStartTime;

    private Integer hourEndTime;

    private Integer minuteEndTime;

    private Integer secondEndTime;

    private String company;

    private String department;

    private Boolean isDeleted;

    private String createdBy;

    private ZonedDateTime createdDate;

    private String updatedBy;

    private ZonedDateTime updatedAt;

    private String deletedBy;

    private ZonedDateTime deletedAt;

}
