package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.PaymentRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentRequestDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private String referenceNumber;

    private Integer order;

    private UUID createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO createdByEmployee;

    private UUID employeeId;

    private ZonedDateTime createdDate;

    private RequestTypeEnum type;

    private UUID departmentId;

    private String company;

    private String content;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachments;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private ZonedDateTime paymentDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime reimbursementDate;

    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private RequestStatus status;

    private UUID supplierId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO suppliers;

    private String paymentVoucher;

    private BigDecimal paymentVoucherAmount;

    private Collection<RequestApprovalDTO> requestApprovals;

    @JsonIgnoreProperties(value = {"requestApprovals", "incomingWarehouse", "suppliers", "paymentDetails"}, allowSetters = true)
    private Collection<PaymentDetailDTO> paymentDetails;

    private Collection<ReimbursementDTO> reimbursementDTOS;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO employee;

    private BigDecimal overSpent;

    private BigDecimal remainingBalance;

    private String paymentTermText;

    public PaymentRequest toEntity() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(this.id);
        paymentRequest.setCode(this.code);
        paymentRequest.setOrder(this.order);
        paymentRequest.setCreatedBy(this.createdBy);
        paymentRequest.setEmployeeId(this.employeeId);
        paymentRequest.setCreatedDate(this.createdDate);
        paymentRequest.setType(this.type);
        paymentRequest.setDepartmentId(this.departmentId);
        paymentRequest.setCompany(this.company);
        paymentRequest.setContent(this.content);
        paymentRequest.setAttachments(this.attachments);
        paymentRequest.setTotalAmount(Objects.isNull(this.totalAmount) ? BigDecimal.ZERO : this.totalAmount);
        paymentRequest.setPaidAmount(Objects.isNull(this.paidAmount) ? BigDecimal.ZERO : this.paidAmount);
        paymentRequest.setRemainingAmount(paymentRequest.getTotalAmount().subtract(paymentRequest.getPaidAmount()));
        paymentRequest.setPaymentDate(this.paymentDate);
        paymentRequest.setNote(this.note);
        paymentRequest.setStatus(this.status);
        paymentRequest.setSupplierId(this.supplierId);
        paymentRequest.setPaymentVoucher(this.paymentVoucher);
        paymentRequest.setPaymentVoucherAmount(this.paymentVoucherAmount);

        return paymentRequest;
    }
}
