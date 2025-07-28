package com.masi.production.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class WorkOrderRO {
    private String name;
    private LocalDate startDate;

    private LocalDate endDate;

}
