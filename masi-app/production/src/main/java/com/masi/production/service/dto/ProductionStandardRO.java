package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;


@Data
public class ProductionStandardRO {
    private String name;
    private LocalDate startDate;

    private LocalDate endDate;

    private String company;

    private String department;

    private UUID employeeId;

    private String searchString;
    private Collection<String> statuses;

}
