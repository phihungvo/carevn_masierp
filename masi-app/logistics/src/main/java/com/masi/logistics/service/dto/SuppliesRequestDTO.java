package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.CSV.CSVColumn;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.enumeration.RequestStatus;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.SuppliesRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesRequestDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code; // Số chứng từ

    private String requestNumber; // Số hoá đơn

    private LocalDate requestDate; // Ngày hoá đơn

    private UUID requestByEmployeeId; // Người đại diện

    private UUID departmentId; // Bộ phận

    private RequestStatus requestStatus; // Trạng thái yêu cầu

    private BigDecimal totalAmount; // Tổng cộng

    private Double vat; // % VAT

    private BigDecimal totalAmountAfterVat; // Tổng cộng sau VAT

    private String note; // Ghi chú

    private UUID createdByEmployeeId; // Người lập

    private EmployeeDTO createdByEmployee;

    private LocalDate createdDateByEmployee; // Ngày lập

    private String context; // Nội dung

    private LocalDate issueDate; // Ngày phát sinh

    private String contractId; // Mã hợp đồng

    private String contractCode; // Mã hợp đồng

    private String contractContent; // Nội dung hợp đồng

    private LocalDate contractDate; // Ngày tạo hợp đồng

    private UUID supplierId; // Nhà cung cấp

    private SuppliersDTO supplier;

    private String supplierFullName;

    private String supplierPosition;

    private String supplierPhone;

    private String supplierEmail;

    private String taxNumber; // MST

    private String tel; // Tel

    private String paymentMethod; // Hình thức thanh toán

    private String currency; // Đơn vị tiền tệ

    private Float exchangeRate; // Tỷ giá

    private String warehouseId; // Thông tin kho

    private BigDecimal totalQuantity; // Tổng số lượng

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachedFiles; // Đính kèm

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;


    private Collection<SuppliesItemDTO>  suppliesItemDTO;

    private Collection<SupplierContractDTO> supplierContracts;

    private Collection<RequestApprovalDTO> requestApprovals;

    private UUID requestTypeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliesRequestTypeDTO requestType;

    private BigDecimal deliveredQuantity;

    private BigDecimal remainingQuantity;

    private Boolean isReview = true;

    public BigDecimal getTotalAmount() {
        if (suppliesItemDTO == null || suppliesItemDTO.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return suppliesItemDTO.stream()
                .map(SuppliesItemDTO::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmountAfterVat() {
        if (suppliesItemDTO == null || suppliesItemDTO.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return suppliesItemDTO.stream()
                .map(SuppliesItemDTO::getTotalAmountAfterVat)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalQuantity() {
        if (suppliesItemDTO == null || suppliesItemDTO.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return suppliesItemDTO.stream()
                .map(SuppliesItemDTO::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
