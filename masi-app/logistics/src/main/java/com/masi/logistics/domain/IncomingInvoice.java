package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import com.masi.logistics.domain.enumeration.IncomingInvoiceType;
import io.r2dbc.postgresql.codec.Json;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IncomingInvoice.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("incoming_invoice")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomingInvoice extends AbstractAuditingEntity<UUID> implements Serializable, Persistable<UUID> {

    public static final String ENTITY_NAME = "incoming_invoice".toUpperCase();
    @Serial
    private static final long serialVersionUID = 7239834847185735680L;

    @Id
    @Column("id")
    private UUID id;

    @Column("invoice_no")
    private String invoiceNo;

    @Column("series")
    private String series;

    @Column("invoice_date")
    private ZonedDateTime invoiceDate; // ngày hoá đơn

    @Column("employee_id")
    private UUID employeeId;

    @Column("supplier_id")
    private UUID supplierId;

    @Column("need_approval")
    private Boolean needApproval;

    @Column("supplier_contract_id")
    private UUID supplierContractId;

    @Transient
    @JsonIgnoreProperties(value = {"incomingInvoices"}, allowSetters = true)
    private SupplierContract supplierContract;

    @Column("inventory_in_id")
    private UUID inventoryInId;

    @Column("payment_status")
    private String paymentStatus;

    @Column("payment_method")
    private String paymentMethod;

    @Column("debt_days")
    private Double debtDays;

    @Column("currency_id")
    private UUID currencyId;

    @Column("currency_code")
    private String currencyCode;

    @Column("currency_rate")
    private Double currencyRate;

    @Column("department_id")
    private UUID departmentId;

    @Column("content")
    private String content;

    @Column("note")
    private String note;

    @Column("attachments")
    private Json attachments;

    @Column("document_id")
    private UUID documentId;

    @Transient
    @JsonIgnoreProperties(value = {"incomingInvoices"}, allowSetters = true)
    private PaymentRequest paymentRequest;

    @Column("pattern_no")
    private String patternNo; // mẫu số

    @Column("reimbursement_id")
    private UUID reimbursementId;

    @Transient
    @JsonIgnoreProperties(value = {"incomingInvoices"}, allowSetters = true)
    private PaymentRequest reimbursement;

    @Column("invoice_type")
    private IncomingInvoiceType invoiceType;

    @Column("total_quantity")
    private BigDecimal  totalQuantity;

    @Column("status")
    private IncomingInvoiceStatus status;

    @Column("total_amount")
    private BigDecimal totalAmount; // tong tien

    @Column("supplier_full_name")
    private String supplierFullName;

    @Column("supplier_phone")
    private String supplierPhone;

    @Column("supplier_email")
    private String supplierEmail;

    @Column("import_invoice_id")
    private UUID importInvoiceId;

    @Column("import_fee")
    private BigDecimal importFee;


    @Column("total_amount_vat")
    private BigDecimal totalAmountVat; // tong tiền vat

    @Column("total_fee_after_import")
    private BigDecimal totalFeeAfterImport; // tong tien sau khi nhap

    @Column("total_amount_supplies")
    private BigDecimal totalAmountSupplies; // tien hang

    @Column("total_pre_import_fee")
    private BigDecimal totalPreImportFee; // phí trước nhập khẩu

    @Column("total_import_tax") // thuế nhập khẩu
    private BigDecimal totalImportTax;

    @Column("total_env_tax") // thuế môi trường
    private BigDecimal totalEnvTax;

    @Column("total_vat_percentage")
    private Double totalVatPercentage; // vat

    @Column("grand_total")
    private BigDecimal grandTotal; // tien nhap kho

    @Column("total_amount_after_vat")
    private BigDecimal totalAmountAfterVat;

    @Column("is_invoice")
    private Boolean isInvoice;

    @Column("total_amount_import_stock")
    private BigDecimal totalAmountImportStock;

    @Column("order_created_at")
    private ZonedDateTime orderCreatedAt;

    @Transient
    private Suppliers suppliers;

    @Transient
    private Currency currency;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public IncomingInvoice id(UUID id) {
        this.setId(id);
        return this;
    }

    public IncomingInvoice invoiceNo(String invoiceNo) {
        this.setInvoiceNo(invoiceNo);
        return this;
    }

    public IncomingInvoice invoiceDate(ZonedDateTime invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public IncomingInvoice employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public IncomingInvoice departmentId(UUID departmentId) {
        this.setDepartmentId(departmentId);
        return this;
    }

    public IncomingInvoice content(String content) {
        this.setContent(content);
        return this;
    }

    public IncomingInvoice totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public IncomingInvoice note(String note) {
        this.setNote(note);
        return this;
    }

    public IncomingInvoice attachments(Json attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public IncomingInvoice createBy(String createBy) {
        this.setCreatedBy(createBy);
        return this;
    }

    public IncomingInvoice createAt(ZonedDateTime createAt) {
        this.setCreatedAt(createAt);
        return this;
    }

    public IncomingInvoice updateBy(String updateBy) {
        this.setUpdatedBy(updateBy);
        return this;
    }

    public IncomingInvoice updateAt(ZonedDateTime updateAt) {
        this.setUpdatedAt(updateAt);
        return this;
    }

    public IncomingInvoice deleteBy(String deleteBy) {
        this.setDeletedBy(deleteBy);
        return this;
    }

    public IncomingInvoice deleteAt(ZonedDateTime deleteAt) {
        this.setDeletedAt(deleteAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public IncomingInvoice setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public IncomingInvoice series(String series) {
        this.setSeries(series);
        return this;
    }

    public IncomingInvoice supplierContractId(UUID supplierContractId) {
        this.setSupplierContractId(supplierContractId);
        return this;
    }

    public IncomingInvoice inventoryInId(UUID inventoryInId) {
        this.setInventoryInId(inventoryInId);
        return this;
    }

    public IncomingInvoice paymentStatus(String paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public IncomingInvoice paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public IncomingInvoice debtDays(Double debtDays) {
        this.setDebtDays(debtDays);
        return this;
    }

    public IncomingInvoice currencyId(UUID currencyId) {
        this.setCurrencyId(currencyId);
        return this;
    }

    public IncomingInvoice currencyRate(Double currencyRate) {
        this.setCurrencyRate(currencyRate);
        return this;
    }

    public IncomingInvoice totalQuantity(BigDecimal totalQuantity) {
        this.setTotalQuantity(totalQuantity);
        return this;
    }

    public IncomingInvoice importFee(BigDecimal importFee) {
        this.setImportFee(importFee);
        return this;
    }

    public IncomingInvoice totalAmountVat(BigDecimal totalAmountVat) {
        this.setTotalAmountVat(totalAmountVat);
        return this;
    }

    public IncomingInvoice grandTotal(BigDecimal grandTotal) {
        this.setGrandTotal(grandTotal);
        return this;
    }

}
