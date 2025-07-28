package com.masi.employee.service.dto;

import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.time.ZonedDateTime;

@ParameterObject
@Data
public class UniformQuery {
    private String search;

    private String company;

    private String status;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;
}
