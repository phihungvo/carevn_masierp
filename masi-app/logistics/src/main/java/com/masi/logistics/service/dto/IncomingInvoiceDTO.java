package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmbedFile;
import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import com.masi.logistics.domain.enumeration.IncomingInvoiceType;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A DTO for the {@link com.masi.logistics.domain.IncomingInvoice} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomingInvoiceDTO extends AuditingDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id = UUID.randomUUID();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String invoiceNo;

    private ZonedDateTime invoiceDate;

    private UUID employeeId;

    private EmployeeDTO employee;

    private UUID departmentId;

    private String content;

    private BigDecimal totalAmount;

    private String note;

    private UUID documentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentRequestDTO paymentRequest;

    private IncomingInvoiceType invoiceType;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachments;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<EmbedFile> files;

    private String series;

    private UUID supplierContractId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value = {"incomingInvoices", "outgoingInvoices", "incomingInvoice", "invoice"})
    private SupplierContractDTO supplierContract;

    private UUID supplierId;

    private UUID inventoryInId;

    private String paymentStatus;

    private String paymentMethod;

    private Double debtDays;

    private UUID currencyId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CurrencyDTO currency;

    private String currencyCode;

    private Double currencyRate;

    private BigDecimal totalQuantity;

    private BigDecimal importFee;

    private Double vat;

    private BigDecimal totalAmountVat;

    private BigDecimal grandTotal;

    private String patternNo;

    private UUID reimbursementId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentRequestDTO reimbursement;

    private BigDecimal totalFeeAfterImport; // tong tien sau khi nhap

    private BigDecimal totalAmountSupplies; // tien hang

    private BigDecimal totalPreImportFee; // phí trước nhập khẩu

    private BigDecimal totalImportTax;// tong thue nhap khau

    private BigDecimal totalEnvTax; // tong thue moi truong

    private Double totalVat; // vat

    private BigDecimal totalAmountAfterVat;

    private String supplierFullName;

    private String supplierEmail;

    private String supplierPhone;

    private UUID importInvoiceId;

    private ZonedDateTime orderCreatedAt;

    private EmployeeDTO createdByEmployee;

    public Json getAttachments() {
        if (attachments != null) {
            return attachments;
        }
        if (files != null) {
            return EmbedFile.fromList(files);
        }
        return Json.of("[]");
    }

    private Collection<InvoiceSuppliesDTO> invoiceSupplies;

    private Collection<RelatedCostsDTO> relatedCosts;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Collection<RequestApprovalDTO> requestApprovals;

    private Collection<UUID> inventoryIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties(value = {"incomingInvoices", "outgoingInvoices", "incomingInvoice", "invoice"})
    private Collection<InventoriesDTO> inventories;

    private Boolean needApproval = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private IncomingInvoiceStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO suppliers;

    private BigDecimal totalAmountImportStock;

    private Boolean isInvoice = false;

    public void setInvoiceSupplies(Collection<InvoiceSuppliesDTO> invoiceSupplies) {
        this.invoiceSupplies = invoiceSupplies;
        calcTotalQuantity();
        calcTotalAmountSupplies();
        calcTotalVat();
        calcTotalAmountVat();
        calcTotalFeeAfterImport();
        calcTotalPreImportFee();
        calcTotalImportTax();
        calcTotalEnvTax();
        calcTotalAmount();
        calcTotalAmountAfterVat();
        calcTotalAmountImportStock();
        calcGrandTotal();
        calcImportFee();
    }

    private void calcTotalQuantity() {
        BigDecimal totalQuantityValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalQuantityValue = totalQuantityValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getQuantity)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        this.totalQuantity = totalQuantityValue;

    }

    public void calcTotalAmountSupplies() {
        BigDecimal totalAmountSuppliesValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalAmountSuppliesValue = totalAmountSuppliesValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalAmountSupplies = totalAmountSuppliesValue;
    }

    public void calcTotalVat() {
        double totalVatValue = 0.0;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalVatValue = totalVatValue + invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getVat)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
        }

        this.totalVat = totalVatValue;
    }

    public void calcTotalAmountVat() {
        BigDecimal totalAmountVatValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalAmountVatValue = totalAmountVatValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getVatAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalAmountVat = totalAmountVatValue;
    }

    public void calcGrandTotal() {
        BigDecimal grandTotalValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            grandTotalValue = grandTotalValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getGrandTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.grandTotal = grandTotalValue;
    }

    public void calcTotalFeeAfterImport() {
        BigDecimal totalFeeAfterImportValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalFeeAfterImportValue = totalFeeAfterImportValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getPostImportFee)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalFeeAfterImport = totalFeeAfterImportValue;
    }

    public void calcTotalPreImportFee() {
        BigDecimal totalPreImportFeeValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalPreImportFeeValue = totalPreImportFeeValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getPreImportFee)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalPreImportFee = totalPreImportFeeValue;
    }

    public void calcTotalImportTax() {
        BigDecimal totalImportTaxValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalImportTaxValue = totalImportTaxValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getImportTaxAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalImportTax = totalImportTaxValue;
    }

    public void calcTotalEnvTax() {
        BigDecimal totalEnvTaxValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalEnvTaxValue = totalEnvTaxValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getEnvFeeAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.totalEnvTax = totalEnvTaxValue;
    }

    public void calcImportFee() {
        BigDecimal importFeeValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            importFeeValue = importFeeValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getImportTaxAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        this.importFee = importFeeValue;
    }

    public void calcTotalAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalAmount = totalAmount.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        this.totalAmount = totalAmount;
    }

    public void calcTotalAmountAfterVat() {
        BigDecimal totalAmountAfterVatValue = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalAmountAfterVatValue = totalAmountAfterVatValue.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getTotalAmountAfterVat)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        this.totalAmountAfterVat = totalAmountAfterVatValue;
    }

    public void calcTotalAmountImportStock() {
        BigDecimal totalAmountImportStock = BigDecimal.ZERO;
        if (invoiceSupplies != null && !invoiceSupplies.isEmpty()) {
            totalAmountImportStock = totalAmountImportStock.add(invoiceSupplies.stream()
                .map(InvoiceSuppliesDTO::getTotalAmountImportStock)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        this.totalAmountImportStock = totalAmountImportStock;
    }


}
