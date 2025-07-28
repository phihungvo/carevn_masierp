package com.masi.production.service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductMaintainQuery {
    private String search;
    private LocalDate manufactureStartDate;
    private LocalDate manufactureEndDate;
    private LocalDate expiredStartDate;
    private LocalDate expiredEndDate;
    private String company;
    private String department;

    private Boolean isProductRoutingSpecified;

}
