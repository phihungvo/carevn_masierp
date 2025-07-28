package com.masi.production.service.dto;

import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.StatusEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Request object for querying manufacture orders.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufactureOrderRO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<StatusEntity> statuses;
    private String searchString;
    private String company;
    private String department;
    private String typePage;
    private Boolean isProductPackageSpecified;
    private Boolean isProductMaintainSpecified;
    private Boolean isProductRoutingSpecified;

    private ManufactureOrderType manufactureOrderType;

}
