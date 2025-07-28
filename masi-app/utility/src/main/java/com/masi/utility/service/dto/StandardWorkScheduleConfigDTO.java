package com.masi.utility.service.dto;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.utility.domain.StandardWorkScheduleConfig} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StandardWorkScheduleConfigDTO implements Serializable {

    private UUID id;

    private String name;

    private Integer dayOfWeek;

    private Integer numberOfShifts;

    private Integer workHours;

    private String department;

    private String company;

    private String departmentType;

    private ZonedDateTime updatedAt;

    private String updatedBy;

}
