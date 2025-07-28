package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PaymentRequest.
 */
@Data
@Table("payment_request")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentRequest implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;
    public static final String ENTITY_NAME = "masiLogisticsPaymentRequest";

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("reference_number")
    private String referenceNumber;

    @Column("status")
    private RequestStatus status;

    @Column("jhi_order")
    private Integer order;

    @Column("created_by")
    private UUID createdBy;

    @Column("employee_id")
    private UUID employeeId;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("type")
    private RequestTypeEnum type;

    @Column("department_id")
    private UUID departmentId;

    @Column("company")
    private String company;

    @Column("content")
    private String content;

    @Column("attachments")
    private Json attachments;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("paid_amount")
    private BigDecimal paidAmount;

    @Column("remaining_amount")
    private BigDecimal remainingAmount;

    @Column("payment_date")
    private ZonedDateTime paymentDate;

    @Column("reimbursement_date")
    private ZonedDateTime reimbursementDate;

    @Column("note")
    private String note;

    @Column("supplier_id")
    private UUID supplierId;

    @Transient
    private Suppliers suppliers;

    //// DNTU
    // thông tin phiếu chi
    @Column("payment_voucher")
    private String paymentVoucher;

    @Column("payment_voucher_amount")
    private BigDecimal paymentVoucherAmount;
    ////

    //// HTU
    @Column("remaining_balance") // số tiền tạm ứng còn lại
    private BigDecimal remainingBalance;

    @Column("over_spent") // tổng số tiền tạm ứng vượt quá
    private BigDecimal overSpent;
    ////

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_by")
    private UUID deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("payment_term_text")
    private String paymentTermText;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PaymentRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public PaymentRequest code(String code) {
        this.setCode(code);
        return this;
    }

    public PaymentRequest order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public PaymentRequest createdBy(UUID createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public PaymentRequest employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public PaymentRequest createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public PaymentRequest type(RequestTypeEnum type) {
        this.setType(type);
        return this;
    }

    public PaymentRequest departmentId(UUID departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public PaymentRequest company(String company) {
        this.setCompany(company);
        return this;
    }

    public PaymentRequest content(String content) {
        this.setContent(content);
        return this;
    }

    public PaymentRequest attachments(Json attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public PaymentRequest totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public PaymentRequest paidAmount(BigDecimal paidAmount) {
        this.setPaidAmount(paidAmount);
        return this;
    }

    public PaymentRequest remainingAmount(BigDecimal remainingAmount) {
        this.setRemainingAmount(remainingAmount);
        return this;
    }

    public PaymentRequest paymentDate(ZonedDateTime paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public PaymentRequest note(String note) {
        this.setNote(note);
        return this;
    }

    public PaymentRequest status(RequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public PaymentRequest supplierId(UUID supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PaymentRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
