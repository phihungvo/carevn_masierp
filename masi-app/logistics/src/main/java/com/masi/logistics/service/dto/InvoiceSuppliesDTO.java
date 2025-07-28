package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InvoiceSupplies} entity.
 *  *     product_code VARCHAR(255),            -- Product code (Mã hàng hóa)
 *     description TEXT,                      -- Description (Diễn giải)
 *     unit VARCHAR(100),                     -- Unit of measurement (DVT)
 *     quantity NUMERIC(10, 2),               -- Quantity (Số lượng)
 *     unit_price NUMERIC(15, 2),             -- Unit price (Đơn giá)
 *     total_price NUMERIC(15, 2),            -- Total price (Thành tiền)
 *     vat_percentage NUMERIC(5, 2),          -- VAT percentage (% VAT)
 *     vat_amount NUMERIC(15, 2),             -- VAT amount (VAT)
 *     pre_import_fee NUMERIC(15, 2),         -- Pre-import fee (Phí trước nhập khẩu)
 *     import_tax_percentage NUMERIC(5, 2),   -- Import tax percentage (% NK)
 *     import_tax_amount NUMERIC(15, 2),      -- Import tax amount (Thuế NK)
 *     env_fee_percentage NUMERIC(5, 2),      -- Environmental fee percentage (% MT)
 *     env_fee_amount NUMERIC(15, 2),         -- Environmental fee amount (Thuế MT)
 *     post_import_fee NUMERIC(15, 2),        -- Post-import fee (Phí sau nhập khẩu)
 *     grand_total NUMERIC(15, 2),            -- Grand total (Tổng cộng)
 *     notes TEXT                             -- Notes (Ghi chú)
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceSuppliesDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID itemId;

    private String itemName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO item;

    private String detail1;

    private String detail2;

    private UUID invoiceId;

    private UUID supplyId;

    private BigDecimal quantity;

    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal total;

    private BigDecimal vat;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal vatAmount;

    private BigDecimal preImportFee;

    private BigDecimal importTaxPercentage; // phần trăm thuế nhập khẩu

    private BigDecimal envFeePercentage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal importTaxAmount; // thuế nhập khẩu

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal envFeeAmount;

    private BigDecimal postImportFee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal grandTotal;

    private String note;

    private UUID vatId;

    private BigDecimal totalAmountAfterVat;

    private BigDecimal totalAmountImportStock;

     public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        calculateTotalPrice(); // Auto-calculate total price when quantity is set
        calculateTotalAmountAfterVat(); // Auto-calculate grand total
        calculateEnvFeeAmount(); // Auto-calculate environmental fee amount
        calculateImportTaxAmount(); // Auto-calculate import tax amount
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        calculateTotalPrice(); // Auto-calculate total price when unit price is set
        calculateTotalAmountAfterVat(); // Auto-calculate grand total
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
        calculateVatAmount(); // Auto-calculate VAT amount
        calculateTotalAmountAfterVat(); // Auto-calculate grand total
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    private void calculateTotalPrice() {
        if (quantity != null && price != null) {
            this.total = quantity.multiply(price);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }

    private void calculateVatAmount() {
        if (total != null && vat != null) {
            this.vatAmount = total.multiply(vat).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            this.vatAmount = BigDecimal.ZERO;
        }
    }

    private void calculateTotalAmountAfterVat() {
        if (total != null && vatAmount != null) {
            this.totalAmountAfterVat = total.add(vatAmount);
        } else {
            this.totalAmountAfterVat = BigDecimal.ZERO;
        }
    }

    public void setImportTaxPercentage(BigDecimal importTaxPercentage) {
        this.importTaxPercentage = importTaxPercentage != null ? importTaxPercentage : BigDecimal.ZERO;
        calculateImportTaxAmount(); // Auto-calculate import tax amount
        calculateTotalAmountImportStock();
        calculateGrandTotal(); // Auto-calculate grand total
    }

    private void calculateImportTaxAmount() {
        if (total != null && importTaxPercentage != null) {
            this.importTaxAmount = total.multiply(importTaxPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            this.importTaxAmount = BigDecimal.ZERO;
        }
    }

    public void setEnvFeePercentage(BigDecimal envFeePercentage) {
        this.envFeePercentage = envFeePercentage != null ? envFeePercentage : BigDecimal.ZERO;
        calculateEnvFeeAmount(); // Auto-calculate environmental fee amount
        calculateGrandTotal(); // Auto-calculate grand total
    }

    private void calculateEnvFeeAmount() {
        if (total != null && envFeePercentage != null) {
            this.envFeeAmount = total.multiply(envFeePercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            this.envFeeAmount = BigDecimal.ZERO;
        }
    }

    public void setPostImportFee(BigDecimal postImportFee) {
        this.postImportFee = postImportFee != null ? postImportFee : BigDecimal.ZERO;
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setPreImportFee(BigDecimal preImportFee) {
        this.preImportFee = preImportFee != null ? preImportFee : BigDecimal.ZERO;
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setImportTaxAmount(BigDecimal importTaxAmount) {
        this.importTaxAmount = importTaxAmount != null ? importTaxAmount : BigDecimal.ZERO;
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setEnvFeeAmount(BigDecimal envFeeAmount) {
        this.envFeeAmount = envFeeAmount != null ? envFeeAmount : BigDecimal.ZERO;
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    public void setTotalAmountImportStock(BigDecimal totalAmountImportStock) {
        this.totalAmountImportStock = totalAmountImportStock != null ? totalAmountImportStock : BigDecimal.ZERO;
        calculateTotalAmountImportStock(); // Auto-calculate total amount import stock
        calculateGrandTotal(); // Auto-calculate grand total
    }

    private void calculateTotalAmountImportStock() {
        var totalAmountImportStockValue = BigDecimal.ZERO;
        if (preImportFee != null)
            totalAmountImportStockValue = totalAmountImportStockValue.add(preImportFee);
        if (importTaxAmount != null)
            totalAmountImportStockValue = totalAmountImportStockValue.add(importTaxAmount);
        if (envFeeAmount != null)
            totalAmountImportStockValue = totalAmountImportStockValue.add(envFeeAmount);
        if (postImportFee != null)
            totalAmountImportStockValue = totalAmountImportStockValue.add(postImportFee);
        this.totalAmountImportStock = totalAmountImportStockValue;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal != null ? grandTotal : BigDecimal.ZERO;
        calculateGrandTotal(); // Auto-calculate grand total
     }

    private void calculateGrandTotal(){
        var grandTotalValue = BigDecimal.ZERO;
        if (totalAmountAfterVat != null)
            grandTotalValue = grandTotalValue.add(totalAmountAfterVat);
        if (totalAmountImportStock != null)
            grandTotalValue = grandTotalValue.add(totalAmountImportStock);
        this.grandTotal = grandTotalValue;
    }

}
