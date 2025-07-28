package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmbedFile;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.enumeration.ContractStatus;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.SupplierContract} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContractDTO extends AuditingDto implements Serializable {

    private UUID id=UUID.randomUUID();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String contractCode;

    private String contractName;

    private UUID supplierId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO supplier;

    private LocalDate contractDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal contractAmount;

    private Double paymentTermNumber;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountAfterVat;

    private BigDecimal totalQuantity;

    private String supplierFullName;

    private String supplierPosition;

    private String supplierPhone;

    private String supplierEmail;

    private String note;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachments;

    @Schema(allOf = {SupplierContractDetailDTO.class})
    private Collection<SupplierContractDetailDTO> supplierContractDetails;

    // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ContractStatus status;

    private Collection<EmbedFile> files;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Collection<IncomingInvoiceDTO> incomingInvoices;

    private UUID suppliesRequestId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliesRequestDTO suppliesRequest;

    private LocalDate deliveryEstDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    private Collection<RequestApprovalDTO> requestApprovals;

    private Collection<RequestApprovalDTO> liquidationRequestApprovals;

    private ContractStatus.DeliveryStatus deliveryStatus;

    public BigDecimal getTotalPrice() {
        if (supplierContractDetails != null) {
            return supplierContractDetails.stream()
                .map(SupplierContractDetailDTO::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmount() {
        if (supplierContractDetails != null) {
            return supplierContractDetails.stream()
                .map(SupplierContractDetailDTO::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmountAfterVat() {
        if (supplierContractDetails != null) {
            return supplierContractDetails.stream()
                .map(SupplierContractDetailDTO::getTotalAmountAfterVat)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalQuantity() {
        if (supplierContractDetails != null) {
            return supplierContractDetails.stream()
                .map(SupplierContractDetailDTO::getQuantity)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }
}
