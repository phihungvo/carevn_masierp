package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.RequestStatus;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SuppliesRequest.
 */
@Data
@Table("supplies_request")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesRequest implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;
    public static final String ENTITY_NAME = "masiLogisticsSuppliesRequest";


    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code; // Số chứng từ

    @NotNull(message = "must not be null")
    @Column("request_number")
    private String requestNumber; // Số hoá đơn

    @NotNull(message = "must not be null")
    @Column("request_date")
    private LocalDate requestDate; // Ngày hoá đơn

    @Column("request_by_employee_id")
    private UUID requestByEmployeeId; // Người đại diện

    @Column("created_by_employee_id")
    private UUID createdByEmployeeId; // Người lập

    @Column("department_id")
    private UUID departmentId; // Bộ phận

    @Column("created_date_by_employee")
    private LocalDate createdDateByEmployee; // Ngày lập

    @Column("context")
    private String context; // Nội dung

    @NotNull(message = "must not be null")
    @Column("request_status")
    private RequestStatus requestStatus;

    @Column("issue_date")
    private LocalDate issueDate; // Ngày phát sinh

    @Column("contract_id")
    private String contractId;

    @Column("contract_code")
    private String contractCode; // Mã hợp đồng

    @Column("contract_content")
    private String contractContent; // Nội dung hợp đồng

    @Column("contract_date")
    private LocalDate contractDate; // Ngày tạo

    @Column("supplier_id")
    private UUID supplierId; // Nhà cung cấp

    @Column("tax_number")
    private String taxNumber; // MST

    @Column("tel")
    private String tel; // Tel

    @Column("payment_method")
    private String paymentMethod; // Hình thức thanh toán

    @Column("currency")
    private String currency; // Đơn vị tiền tệ

    @Column("exchange_rate")
    private Float exchangeRate; // Tỷ giá

    @Column("warehouse_id")
    private String warehouseId; // Thông tin kho

    @Column("note")
    private String note; // Ghi chú

    @NotNull(message = "must not be null")
    @Column("total_amount")
    private BigDecimal totalAmount; // Tổng cộng

    @Column("vat_percentage")
    private BigDecimal vatPercentage; // % VAT

    @Column("total_vat")
    private BigDecimal totalVat; // Tổng VAT

    @Column("total_paid")
    private BigDecimal totalPaid; // Tổng tiền đã thanh toán

    @Column("attached_files")
    private Json attachedFiles; // Đính kèm

    ///////////////////////////////////

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("vat")
    private Double vat;

    @Column("total_amount_after_vat")
    private BigDecimal totalAmountAfterVat;

    @Column("is_review")
    private Boolean isReview;

    @Column("total_quantity")
    private BigDecimal totalQuantity;

    @Column("delivered_quantity")
    private BigDecimal deliveredQuantity;

    @Column("remaining_quantity")
    private BigDecimal remainingQuantity;

    @Column("supplier_full_name")
    private String supplierFullName;

    @Column("supplier_position")
    private String supplierPosition;

    @Column("supplier_phone")
    private String supplierPhone;

    @Column("supplier_email")
    private String supplierEmail;

    @Transient
    @JsonIgnoreProperties(value = { "suppliesRequest" }, allowSetters = true)
    private SuppliesRequestType requestType;

    @Column("request_type_id")
    private UUID requestTypeId;

    @Transient
    private boolean isPersisted;

    @Transient
    private Suppliers supplier;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SuppliesRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public SuppliesRequest code(String code) {
        this.setCode(code);
        return this;
    }

    public SuppliesRequest requestNumber(String requestNumber) {
        this.setRequestNumber(requestNumber);
        return this;
    }

    public SuppliesRequest requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public SuppliesRequest requestByEmployeeId(UUID requestByEmployeeId) {
        this.setRequestByEmployeeId(requestByEmployeeId);
        return this;
    }

    public SuppliesRequest departmentId(UUID departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public SuppliesRequest requestStatus(RequestStatus requestStatus) {
        this.setRequestStatus(requestStatus);
        return this;
    }

    public SuppliesRequest totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public SuppliesRequest note(String note) {
        this.setNote(note);
        return this;
    }

    public SuppliesRequest company(String company) {
        this.setCompany(company);
        return this;
    }

    public SuppliesRequest department(String department) {
        this.setDepartment(department);
        return this;
    }

    public SuppliesRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public SuppliesRequest createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public SuppliesRequest createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public SuppliesRequest updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public SuppliesRequest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public SuppliesRequest deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public SuppliesRequest deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public SuppliesRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
