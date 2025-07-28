package com.masi.employee.service.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ZkbioTimeDto {
    private String id;
//    private String name;
    private String pin;
//    private String readerName;
    private ZonedDateTime eventTime;
    private UUID employeeId;
}
