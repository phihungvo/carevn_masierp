package com.masi.sale.service.dto;

import com.masi.sale.domain.Customer;
import com.masi.sale.domain.QuotationDetail;
import com.masi.sale.domain.enumeration.QuotationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 21321774211L;

    @NotNull(message = "must not be null")
    private UUID id;

    private UUID customerId;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private QuotationStatus status;

    @NotNull(message = "must not be null")
    private String description;

    private String paymentMethod;
    private String paymentMethodEn;

    private String deliveryLocation;
    private String deliveryLocationEn;

    private ZonedDateTime deliveryDate;

    private String packaging;
    private String packagingEn;

    private String minimumWeight;

    private String fileId;

    private String fileName;

    private String rejectNote;

    private String approvalSignFile;
    private String approvalSignName;

    private String customerRejectNote;

    private UUID customerApproverId;

    private UUID approverId;

    private EmployeeDTO approver;

    private String priceType;

    private String priceTypeEn;

    private String materialCriteria;

    private String materialCriteriaEn;

    private EmployeeDTO customerApprover;

    private EmployeeDTO createdByEmployee;

    private EmployeeDTO updatedByEmployee;

    private String company;

    private String department;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    private String updatedBy;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private String createdBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private ZonedDateTime deletedDate;

    private String deletedBy;

    private Set<QuotationDetail> quotationDetails = new HashSet<>();

    @Schema(allOf = {Customer.class})
    private CustomerDTO customer;
    private ZonedDateTime processAt;

}
