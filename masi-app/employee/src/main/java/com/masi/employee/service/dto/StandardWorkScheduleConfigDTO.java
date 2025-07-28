package com.masi.employee.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;


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
