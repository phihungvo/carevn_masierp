package com.masi.logistics.service.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@ParameterObject
public class IncomingInvoiceQuery {
    private LocalDate startDate;
    private LocalDate endDate;
    private String search;
    @Parameter(hidden = true)
    private String department;
    private String departmentId;
    private String employeeId;
    @Parameter(hidden = true)
    private String companyId;
    private String status;
    @Parameter(hidden = true)
    private String createdBy;
    private Collection<UUID> contractIds;
    private String invoiceType;
    private UUID supplierId;

    private String searchSupplier;

    private UUID supplierContractId;

    private UUID currencyId;

    private Boolean hasInvoice;

    private List<UUID> employeeIds;
}
