package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.WarehouseGroupType;
import com.masi.logistics.service.dto.ContactDTO;
import com.masi.logistics.service.dto.InventoriesDTO;
import com.masi.logistics.service.dto.PaymentRequestDTO;
import io.r2dbc.postgresql.codec.Json;

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
 * A Inventories.
 */
@Data
@Table("inventories")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventories implements Serializable, Persistable<UUID> {

    public static final String ENTITY_NAME = "inventories";
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code") // Mã Phiếu
    private String code;

    @Column("receiver_user_id")
    private UUID receiverUserId;

    @Column("delivery_date") // Ngày nhập kho
    private LocalDate deliveryDate;

    @Column("inventories_type_id")
    private UUID inventoriesTypeId;
    @Transient
    private InventoriesType inventoriesType; // Add InventoriesTypeDTO

    @Column("date_create") // Ngày lập phiếu
    private LocalDate dateCreate;

    @Column("customer_id")
    private UUID customerId;
    @Transient
    private Suppliers customer; // Add SuppliersDTO

    @Column("payment_request_id")
    private UUID paymentRequestId;
    @Transient
    private PaymentRequestDTO paymentRequest;


    @Column("customer_recipient_id") // thông tin người giao
    private UUID customerRecipientId;
    @Transient
    private Suppliers customerRecipient; // Add SuppliersDTO

    @Column("invoice_id")
    private UUID invoiceId;
    @Transient
    private IncomingInvoice invoice; // Add InvoiceSupplies

    @Column("address")
    private String address;

    @Column("note") // Diễn giải
    private String note;

    @Column("purchase_price")
    private BigDecimal purchasePrice;

    @Column("sale_price")
    private BigDecimal salePrice;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("total_quantity")
    private BigDecimal totalQuantity;

    @Column("input_department_id") // Người tạo
    private UUID inputDepartmentId;

    @Column("incoming_warehouse_id") // Kho
    private UUID incomingWarehouseId;
    @Transient
    private Warehouse incomingWarehouse; // Add Warehouse

    @Column("outgoing_warehouse_id")
    private UUID outgoingWarehouseId;
    @Transient
    private Warehouse outgoingWarehouse; // Add Warehouse

    @Column("employee_id")
    private UUID employeeId;

    @Column("order_id") // Hoá đơn đầu vào
    private UUID orderId;

    @Column("supplier_request_id") // Thông tin nhà cung cấp
    private UUID supplierRequestId;
    @Transient
    private SuppliesRequest supplierRequest; // Add SuppliesRequestDTO

    @Column("tax_code")
    private String taxCode;

    @Column("series")
    private String series;

    @Column("currency_code_rate")
    private String currencyCodeRate;

    @Column("exchange_rate")
    private BigDecimal exchangeRate;

    @Column("is_emptiness")
    private Boolean isEmptiness;

    @Column("emptiness_id")
    private UUID emptinessId;

    @Column("file")
    private Json file;

    @Column("attribute")
    private Json attribute;

    @Column("status")
    private StatusEntity status;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_at") // Người tạo
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("purchase_contract_id")
    private UUID purchaseContractId;

    @Transient
    private SupplierContract purchaseContract; // Add ContactDTO

    @Column("production_id")
    private UUID productionId;

    @Column("warehouse_type")
    private WarehouseGroupType warehouseGroupType;

    @Column("is_review")
    private Boolean isReview;

    @Column("is_invoice")
    private Boolean isInvoice;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Inventories id(UUID id) {
        this.setId(id);
        return this;
    }

    public Inventories code(String code) {
        this.setCode(code);
        return this;
    }

    public Inventories receiverUserId(UUID receiverUserId) {
        this.setReceiverUserId(receiverUserId);
        return this;
    }

    public Inventories deliveryDate(LocalDate deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public Inventories inventoriesTypeId(UUID inventoriesTypeId) {
        this.setInventoriesTypeId(inventoriesTypeId);
        return this;
    }

    public Inventories dateCreate(LocalDate dateCreate) {
        this.setDateCreate(dateCreate);
        return this;
    }

    public Inventories customerId(UUID customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public Inventories customerRecipientId(UUID customerRecipientId) {
        this.setCustomerRecipientId(customerRecipientId);
        return this;
    }

    public Inventories invoiceId(UUID invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public Inventories address(String address) {
        this.setAddress(address);
        return this;
    }

    public Inventories note(String note) {
        this.setNote(note);
        return this;
    }

    public Inventories purchasePrice(BigDecimal purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public Inventories salePrice(BigDecimal salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public Inventories totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public Inventories inputDepartmentId(UUID inputDepartmentId) {
        this.setInputDepartmentId(inputDepartmentId);
        return this;
    }

    public Inventories incomingWarehouseId(UUID incomingWarehouseId) {
        this.setIncomingWarehouseId(incomingWarehouseId);
        return this;
    }

    public Inventories outgoingWarehouseId(UUID outgoingWarehouseId) {
        this.setOutgoingWarehouseId(outgoingWarehouseId);
        return this;
    }

    public Inventories employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public Inventories orderId(UUID orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public Inventories supplierRequestId(UUID supplierRequestId) {
        this.setSupplierRequestId(supplierRequestId);
        return this;
    }

    public Inventories taxCode(String taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public Inventories series(String series) {
        this.setSeries(series);
        return this;
    }

    public Inventories currencyCodeRate(String currencyCodeRate) {
        this.setCurrencyCodeRate(currencyCodeRate);
        return this;
    }

    public Inventories exchangeRate(BigDecimal exchangeRate) {
        this.setExchangeRate(exchangeRate);
        return this;
    }

    public Inventories isEmptiness(Boolean isEmptiness) {
        this.setIsEmptiness(isEmptiness);
        return this;
    }

    public Inventories emptinessId(UUID emptinessId) {
        this.setEmptinessId(emptinessId);
        return this;
    }

    public Inventories file(Json file) {
        this.setFile(file);
        return this;
    }

    public Inventories attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public Inventories status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public Inventories isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public Inventories createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public Inventories createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Inventories updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public Inventories updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public Inventories deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public Inventories deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public Inventories company(String company) {
        this.setCompany(company);
        return this;
    }

    public Inventories department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Inventories setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public InventoriesDTO toDto() {
        InventoriesDTO dto = new InventoriesDTO();
        dto.setId(id);
        dto.setCode(code);
        dto.setReceiverUserId(receiverUserId);
        dto.setDeliveryDate(deliveryDate);
        dto.setInventoriesTypeId(inventoriesTypeId);
        dto.setDateCreate(dateCreate);
        dto.setCustomerId(customerId);
        dto.setCustomerRecipientId(customerRecipientId);
        dto.setInvoiceId(invoiceId);
        dto.setAddress(address);
        dto.setNote(note);
        dto.setPurchasePrice(purchasePrice);
        dto.setSalePrice(salePrice);
        dto.setTotalAmount(totalAmount);
        dto.setInputDepartmentId(inputDepartmentId);
        dto.setIncomingWarehouseId(incomingWarehouseId);
        dto.setOutgoingWarehouseId(outgoingWarehouseId);
        dto.setEmployeeId(employeeId);
        dto.setOrderId(orderId);
        dto.setSupplierRequestId(supplierRequestId);
        dto.setTaxCode(taxCode);
        dto.setSeries(series);
        dto.setCurrencyCodeRate(currencyCodeRate);
        dto.setExchangeRate(exchangeRate);
        dto.setIsEmptiness(isEmptiness);
        dto.setEmptinessId(emptinessId);
        dto.setFile(file);
        dto.setAttribute(attribute);
        dto.setStatus(status);
        dto.setIsInvoice(isInvoice);
        return dto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
