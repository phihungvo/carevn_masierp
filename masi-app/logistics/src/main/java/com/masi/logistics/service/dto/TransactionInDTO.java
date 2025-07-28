package com.masi.logistics.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.TransactionIn} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionInDTO implements Serializable {

    private UUID id;

    private String code;

    private BigDecimal unitPrice;

    private Integer quantity;

    private String notes;

    private UUID transactionId;

    private String transactionCode;

    private ZonedDateTime expiredDate;

    private ZonedDateTime manufactureDate;

    private UUID itemId;

    private String itemCode;

    private UUID warehouseId;

    private String warehouseCode;

    private UUID supplierId;

    private String supplierCode;

    private UUID customerId;

    private String customerCode;

    private UUID orderId;

    private String orderCode;

    private UUID manufactureId;

    private String manufactureCode;

    private UUID packingId;

    private String packingCode;

    private ZonedDateTime createAt;

    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private String department;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public ZonedDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(ZonedDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public ZonedDateTime getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(ZonedDateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public UUID getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(UUID manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getManufactureCode() {
        return manufactureCode;
    }

    public void setManufactureCode(String manufactureCode) {
        this.manufactureCode = manufactureCode;
    }

    public UUID getPackingId() {
        return packingId;
    }

    public void setPackingId(UUID packingId) {
        this.packingId = packingId;
    }

    public String getPackingCode() {
        return packingCode;
    }

    public void setPackingCode(String packingCode) {
        this.packingCode = packingCode;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionInDTO)) {
            return false;
        }

        TransactionInDTO transactionInDTO = (TransactionInDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionInDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionInDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", notes='" + getNotes() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionCode='" + getTransactionCode() + "'" +
            ", expiredDate='" + getExpiredDate() + "'" +
            ", manufactureDate='" + getManufactureDate() + "'" +
            ", itemId='" + getItemId() + "'" +
            ", itemCode='" + getItemCode() + "'" +
            ", warehouseId='" + getWarehouseId() + "'" +
            ", warehouseCode='" + getWarehouseCode() + "'" +
            ", supplierId='" + getSupplierId() + "'" +
            ", supplierCode='" + getSupplierCode() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", customerCode='" + getCustomerCode() + "'" +
            ", orderId='" + getOrderId() + "'" +
            ", orderCode='" + getOrderCode() + "'" +
            ", manufactureId='" + getManufactureId() + "'" +
            ", manufactureCode='" + getManufactureCode() + "'" +
            ", packingId='" + getPackingId() + "'" +
            ", packingCode='" + getPackingCode() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }
}
