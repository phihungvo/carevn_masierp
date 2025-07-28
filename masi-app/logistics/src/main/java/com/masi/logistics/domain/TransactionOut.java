package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TransactionOut.
 */
@Table("transaction_out")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionOut implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("transaction_code")
    private String transactionCode;

    @Column("unit_price")
    private BigDecimal unitPrice;

    @Column("quantity")
    private Integer quantity;

    @Column("remain")
    private Integer remain;

    @Column("notes")
    private String notes;

    @Column("type")
    private String type;

    @Column("expired_date")
    private ZonedDateTime expiredDate;

    @Column("manufacture_date")
    private ZonedDateTime manufactureDate;

    @Column("item_id")
    private UUID itemId;

    @Column("item_code")
    private String itemCode;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Column("warehouse_code")
    private String warehouseCode;

    @Column("supplier_id")
    private UUID supplierId;

    @Column("supplier_code")
    private String supplierCode;

    @Column("customer_id")
    private UUID customerId;

    @Column("customer_code")
    private String customerCode;

    @Column("order_id")
    private UUID orderId;

    @Column("order_code")
    private String orderCode;

    @Column("manufacture_id")
    private UUID manufactureId;

    @Column("manufacture_code")
    private String manufactureCode;

    @Column("packing_id")
    private UUID packingId;

    @Column("packing_code")
    private String packingCode;

    @Column("create_at")
    private ZonedDateTime createAt;

    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public TransactionOut id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public TransactionOut code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getTransactionId() {
        return this.transactionId;
    }

    public TransactionOut transactionId(UUID transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCode() {
        return this.transactionCode;
    }

    public TransactionOut transactionCode(String transactionCode) {
        this.setTransactionCode(transactionCode);
        return this;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public TransactionOut unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice.stripTrailingZeros() : null;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public TransactionOut quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRemain() {
        return this.remain;
    }

    public TransactionOut remain(Integer remain) {
        this.setRemain(remain);
        return this;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getNotes() {
        return this.notes;
    }

    public TransactionOut notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return this.type;
    }

    public TransactionOut type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public TransactionOut expiredDate(ZonedDateTime expiredDate) {
        this.setExpiredDate(expiredDate);
        return this;
    }

    public void setExpiredDate(ZonedDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public ZonedDateTime getManufactureDate() {
        return this.manufactureDate;
    }

    public TransactionOut manufactureDate(ZonedDateTime manufactureDate) {
        this.setManufactureDate(manufactureDate);
        return this;
    }

    public void setManufactureDate(ZonedDateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public UUID getItemId() {
        return this.itemId;
    }

    public TransactionOut itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public TransactionOut itemCode(String itemCode) {
        this.setItemCode(itemCode);
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public UUID getWarehouseId() {
        return this.warehouseId;
    }

    public TransactionOut warehouseId(UUID warehouseId) {
        this.setWarehouseId(warehouseId);
        return this;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseCode() {
        return this.warehouseCode;
    }

    public TransactionOut warehouseCode(String warehouseCode) {
        this.setWarehouseCode(warehouseCode);
        return this;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public UUID getSupplierId() {
        return this.supplierId;
    }

    public TransactionOut supplierId(UUID supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return this.supplierCode;
    }

    public TransactionOut supplierCode(String supplierCode) {
        this.setSupplierCode(supplierCode);
        return this;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public TransactionOut customerId(UUID customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public TransactionOut customerCode(String customerCode) {
        this.setCustomerCode(customerCode);
        return this;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public TransactionOut orderId(UUID orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public TransactionOut orderCode(String orderCode) {
        this.setOrderCode(orderCode);
        return this;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public UUID getManufactureId() {
        return this.manufactureId;
    }

    public TransactionOut manufactureId(UUID manufactureId) {
        this.setManufactureId(manufactureId);
        return this;
    }

    public void setManufactureId(UUID manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getManufactureCode() {
        return this.manufactureCode;
    }

    public TransactionOut manufactureCode(String manufactureCode) {
        this.setManufactureCode(manufactureCode);
        return this;
    }

    public void setManufactureCode(String manufactureCode) {
        this.manufactureCode = manufactureCode;
    }

    public UUID getPackingId() {
        return this.packingId;
    }

    public TransactionOut packingId(UUID packingId) {
        this.setPackingId(packingId);
        return this;
    }

    public void setPackingId(UUID packingId) {
        this.packingId = packingId;
    }

    public String getPackingCode() {
        return this.packingCode;
    }

    public TransactionOut packingCode(String packingCode) {
        this.setPackingCode(packingCode);
        return this;
    }

    public void setPackingCode(String packingCode) {
        this.packingCode = packingCode;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public TransactionOut createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public TransactionOut createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public TransactionOut updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public TransactionOut updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return this.deleteAt;
    }

    public TransactionOut deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return this.deleteBy;
    }

    public TransactionOut deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return this.company;
    }

    public TransactionOut company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public TransactionOut department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public TransactionOut setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionOut)) {
            return false;
        }
        return getId() != null && getId().equals(((TransactionOut) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionOut{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionCode='" + getTransactionCode() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", remain=" + getRemain() +
            ", notes='" + getNotes() + "'" +
            ", type='" + getType() + "'" +
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
