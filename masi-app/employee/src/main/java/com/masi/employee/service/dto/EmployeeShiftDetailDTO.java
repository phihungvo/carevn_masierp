package com.masi.employee.service.dto;

import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.EmployeeShiftDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeShiftDetailDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private LocalDate date;

    private UUID timeKeepingId;

    private UUID shiftId;

    private UUID employeeId;

    private Integer hourStartTime;

    private Float durationHours;

    private ZonedDateTime checkInTime;

    private ZonedDateTime checkOutTime;

    private Float completionPercent;

    private String company;

    private String department;

    private Boolean isDeleted;

    private String createdBy;

    private ZonedDateTime createdDate;

    private String updatedBy;

    private ZonedDateTime updatedAt;

    private String deletedBy;

    private ZonedDateTime deletedAt;

    private Boolean isShiftOff;

    private LeaveType leaveDayType;

    private Boolean isWfh;

    private UUID violationId;

    private TimeKeepingViolationType violationType;

    private String note;

    private Boolean isPaidShift;


}
