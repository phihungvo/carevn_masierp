package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.TransactionIn} entity. This class is used
 * in {@link com.masi.logistics.web.rest.TransactionInResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-ins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionInCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private BigDecimalFilter unitPrice;

    private IntegerFilter quantity;

    private StringFilter notes;

    private UUIDFilter transactionId;

    private StringFilter transactionCode;

    private ZonedDateTimeFilter expiredDate;

    private ZonedDateTimeFilter manufactureDate;

    private UUIDFilter itemId;

    private StringFilter itemCode;

    private UUIDFilter warehouseId;

    private StringFilter warehouseCode;

    private UUIDFilter supplierId;

    private StringFilter supplierCode;

    private UUIDFilter customerId;

    private StringFilter customerCode;

    private UUIDFilter orderId;

    private StringFilter orderCode;

    private UUIDFilter manufactureId;

    private StringFilter manufactureCode;

    private UUIDFilter packingId;

    private StringFilter packingCode;

    private ZonedDateTimeFilter createAt;

    private StringFilter createBy;

    private ZonedDateTimeFilter updateAt;

    private StringFilter updateBy;

    private ZonedDateTimeFilter deleteAt;

    private StringFilter deleteBy;

    private StringFilter company;

    private StringFilter department;

    private Boolean distinct;

    public TransactionInCriteria() {}

    public TransactionInCriteria(TransactionInCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.unitPrice = other.optionalUnitPrice().map(BigDecimalFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.transactionId = other.optionalTransactionId().map(UUIDFilter::copy).orElse(null);
        this.transactionCode = other.optionalTransactionCode().map(StringFilter::copy).orElse(null);
        this.expiredDate = other.optionalExpiredDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.manufactureDate = other.optionalManufactureDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(UUIDFilter::copy).orElse(null);
        this.itemCode = other.optionalItemCode().map(StringFilter::copy).orElse(null);
        this.warehouseId = other.optionalWarehouseId().map(UUIDFilter::copy).orElse(null);
        this.warehouseCode = other.optionalWarehouseCode().map(StringFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.supplierCode = other.optionalSupplierCode().map(StringFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.customerCode = other.optionalCustomerCode().map(StringFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(UUIDFilter::copy).orElse(null);
        this.orderCode = other.optionalOrderCode().map(StringFilter::copy).orElse(null);
        this.manufactureId = other.optionalManufactureId().map(UUIDFilter::copy).orElse(null);
        this.manufactureCode = other.optionalManufactureCode().map(StringFilter::copy).orElse(null);
        this.packingId = other.optionalPackingId().map(UUIDFilter::copy).orElse(null);
        this.packingCode = other.optionalPackingCode().map(StringFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createBy = other.optionalCreateBy().map(StringFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updateBy = other.optionalUpdateBy().map(StringFilter::copy).orElse(null);
        this.deleteAt = other.optionalDeleteAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleteBy = other.optionalDeleteBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransactionInCriteria copy() {
        return new TransactionInCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public Optional<BigDecimalFilter> optionalUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    public BigDecimalFilter unitPrice() {
        if (unitPrice == null) {
            setUnitPrice(new BigDecimalFilter());
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public UUIDFilter getTransactionId() {
        return transactionId;
    }

    public Optional<UUIDFilter> optionalTransactionId() {
        return Optional.ofNullable(transactionId);
    }

    public UUIDFilter transactionId() {
        if (transactionId == null) {
            setTransactionId(new UUIDFilter());
        }
        return transactionId;
    }

    public void setTransactionId(UUIDFilter transactionId) {
        this.transactionId = transactionId;
    }

    public StringFilter getTransactionCode() {
        return transactionCode;
    }

    public Optional<StringFilter> optionalTransactionCode() {
        return Optional.ofNullable(transactionCode);
    }

    public StringFilter transactionCode() {
        if (transactionCode == null) {
            setTransactionCode(new StringFilter());
        }
        return transactionCode;
    }

    public void setTransactionCode(StringFilter transactionCode) {
        this.transactionCode = transactionCode;
    }

    public ZonedDateTimeFilter getExpiredDate() {
        return expiredDate;
    }

    public Optional<ZonedDateTimeFilter> optionalExpiredDate() {
        return Optional.ofNullable(expiredDate);
    }

    public ZonedDateTimeFilter expiredDate() {
        if (expiredDate == null) {
            setExpiredDate(new ZonedDateTimeFilter());
        }
        return expiredDate;
    }

    public void setExpiredDate(ZonedDateTimeFilter expiredDate) {
        this.expiredDate = expiredDate;
    }

    public ZonedDateTimeFilter getManufactureDate() {
        return manufactureDate;
    }

    public Optional<ZonedDateTimeFilter> optionalManufactureDate() {
        return Optional.ofNullable(manufactureDate);
    }

    public ZonedDateTimeFilter manufactureDate() {
        if (manufactureDate == null) {
            setManufactureDate(new ZonedDateTimeFilter());
        }
        return manufactureDate;
    }

    public void setManufactureDate(ZonedDateTimeFilter manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public UUIDFilter getItemId() {
        return itemId;
    }

    public Optional<UUIDFilter> optionalItemId() {
        return Optional.ofNullable(itemId);
    }

    public UUIDFilter itemId() {
        if (itemId == null) {
            setItemId(new UUIDFilter());
        }
        return itemId;
    }

    public void setItemId(UUIDFilter itemId) {
        this.itemId = itemId;
    }

    public StringFilter getItemCode() {
        return itemCode;
    }

    public Optional<StringFilter> optionalItemCode() {
        return Optional.ofNullable(itemCode);
    }

    public StringFilter itemCode() {
        if (itemCode == null) {
            setItemCode(new StringFilter());
        }
        return itemCode;
    }

    public void setItemCode(StringFilter itemCode) {
        this.itemCode = itemCode;
    }

    public UUIDFilter getWarehouseId() {
        return warehouseId;
    }

    public Optional<UUIDFilter> optionalWarehouseId() {
        return Optional.ofNullable(warehouseId);
    }

    public UUIDFilter warehouseId() {
        if (warehouseId == null) {
            setWarehouseId(new UUIDFilter());
        }
        return warehouseId;
    }

    public void setWarehouseId(UUIDFilter warehouseId) {
        this.warehouseId = warehouseId;
    }

    public StringFilter getWarehouseCode() {
        return warehouseCode;
    }

    public Optional<StringFilter> optionalWarehouseCode() {
        return Optional.ofNullable(warehouseCode);
    }

    public StringFilter warehouseCode() {
        if (warehouseCode == null) {
            setWarehouseCode(new StringFilter());
        }
        return warehouseCode;
    }

    public void setWarehouseCode(StringFilter warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public UUIDFilter getSupplierId() {
        return supplierId;
    }

    public Optional<UUIDFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public UUIDFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new UUIDFilter());
        }
        return supplierId;
    }

    public void setSupplierId(UUIDFilter supplierId) {
        this.supplierId = supplierId;
    }

    public StringFilter getSupplierCode() {
        return supplierCode;
    }

    public Optional<StringFilter> optionalSupplierCode() {
        return Optional.ofNullable(supplierCode);
    }

    public StringFilter supplierCode() {
        if (supplierCode == null) {
            setSupplierCode(new StringFilter());
        }
        return supplierCode;
    }

    public void setSupplierCode(StringFilter supplierCode) {
        this.supplierCode = supplierCode;
    }

    public UUIDFilter getCustomerId() {
        return customerId;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public void setCustomerId(UUIDFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getCustomerCode() {
        return customerCode;
    }

    public Optional<StringFilter> optionalCustomerCode() {
        return Optional.ofNullable(customerCode);
    }

    public StringFilter customerCode() {
        if (customerCode == null) {
            setCustomerCode(new StringFilter());
        }
        return customerCode;
    }

    public void setCustomerCode(StringFilter customerCode) {
        this.customerCode = customerCode;
    }

    public UUIDFilter getOrderId() {
        return orderId;
    }

    public Optional<UUIDFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public UUIDFilter orderId() {
        if (orderId == null) {
            setOrderId(new UUIDFilter());
        }
        return orderId;
    }

    public void setOrderId(UUIDFilter orderId) {
        this.orderId = orderId;
    }

    public StringFilter getOrderCode() {
        return orderCode;
    }

    public Optional<StringFilter> optionalOrderCode() {
        return Optional.ofNullable(orderCode);
    }

    public StringFilter orderCode() {
        if (orderCode == null) {
            setOrderCode(new StringFilter());
        }
        return orderCode;
    }

    public void setOrderCode(StringFilter orderCode) {
        this.orderCode = orderCode;
    }

    public UUIDFilter getManufactureId() {
        return manufactureId;
    }

    public Optional<UUIDFilter> optionalManufactureId() {
        return Optional.ofNullable(manufactureId);
    }

    public UUIDFilter manufactureId() {
        if (manufactureId == null) {
            setManufactureId(new UUIDFilter());
        }
        return manufactureId;
    }

    public void setManufactureId(UUIDFilter manufactureId) {
        this.manufactureId = manufactureId;
    }

    public StringFilter getManufactureCode() {
        return manufactureCode;
    }

    public Optional<StringFilter> optionalManufactureCode() {
        return Optional.ofNullable(manufactureCode);
    }

    public StringFilter manufactureCode() {
        if (manufactureCode == null) {
            setManufactureCode(new StringFilter());
        }
        return manufactureCode;
    }

    public void setManufactureCode(StringFilter manufactureCode) {
        this.manufactureCode = manufactureCode;
    }

    public UUIDFilter getPackingId() {
        return packingId;
    }

    public Optional<UUIDFilter> optionalPackingId() {
        return Optional.ofNullable(packingId);
    }

    public UUIDFilter packingId() {
        if (packingId == null) {
            setPackingId(new UUIDFilter());
        }
        return packingId;
    }

    public void setPackingId(UUIDFilter packingId) {
        this.packingId = packingId;
    }

    public StringFilter getPackingCode() {
        return packingCode;
    }

    public Optional<StringFilter> optionalPackingCode() {
        return Optional.ofNullable(packingCode);
    }

    public StringFilter packingCode() {
        if (packingCode == null) {
            setPackingCode(new StringFilter());
        }
        return packingCode;
    }

    public void setPackingCode(StringFilter packingCode) {
        this.packingCode = packingCode;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreateAt() {
        return Optional.ofNullable(createAt);
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            setCreateAt(new ZonedDateTimeFilter());
        }
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public StringFilter getCreateBy() {
        return createBy;
    }

    public Optional<StringFilter> optionalCreateBy() {
        return Optional.ofNullable(createBy);
    }

    public StringFilter createBy() {
        if (createBy == null) {
            setCreateBy(new StringFilter());
        }
        return createBy;
    }

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTimeFilter getUpdateAt() {
        return updateAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdateAt() {
        return Optional.ofNullable(updateAt);
    }

    public ZonedDateTimeFilter updateAt() {
        if (updateAt == null) {
            setUpdateAt(new ZonedDateTimeFilter());
        }
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTimeFilter updateAt) {
        this.updateAt = updateAt;
    }

    public StringFilter getUpdateBy() {
        return updateBy;
    }

    public Optional<StringFilter> optionalUpdateBy() {
        return Optional.ofNullable(updateBy);
    }

    public StringFilter updateBy() {
        if (updateBy == null) {
            setUpdateBy(new StringFilter());
        }
        return updateBy;
    }

    public void setUpdateBy(StringFilter updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTimeFilter getDeleteAt() {
        return deleteAt;
    }

    public Optional<ZonedDateTimeFilter> optionalDeleteAt() {
        return Optional.ofNullable(deleteAt);
    }

    public ZonedDateTimeFilter deleteAt() {
        if (deleteAt == null) {
            setDeleteAt(new ZonedDateTimeFilter());
        }
        return deleteAt;
    }

    public void setDeleteAt(ZonedDateTimeFilter deleteAt) {
        this.deleteAt = deleteAt;
    }

    public StringFilter getDeleteBy() {
        return deleteBy;
    }

    public Optional<StringFilter> optionalDeleteBy() {
        return Optional.ofNullable(deleteBy);
    }

    public StringFilter deleteBy() {
        if (deleteBy == null) {
            setDeleteBy(new StringFilter());
        }
        return deleteBy;
    }

    public void setDeleteBy(StringFilter deleteBy) {
        this.deleteBy = deleteBy;
    }

    public StringFilter getCompany() {
        return company;
    }

    public Optional<StringFilter> optionalCompany() {
        return Optional.ofNullable(company);
    }

    public StringFilter company() {
        if (company == null) {
            setCompany(new StringFilter());
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionInCriteria that = (TransactionInCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(transactionCode, that.transactionCode) &&
            Objects.equals(expiredDate, that.expiredDate) &&
            Objects.equals(manufactureDate, that.manufactureDate) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(itemCode, that.itemCode) &&
            Objects.equals(warehouseId, that.warehouseId) &&
            Objects.equals(warehouseCode, that.warehouseCode) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(supplierCode, that.supplierCode) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(customerCode, that.customerCode) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(orderCode, that.orderCode) &&
            Objects.equals(manufactureId, that.manufactureId) &&
            Objects.equals(manufactureCode, that.manufactureCode) &&
            Objects.equals(packingId, that.packingId) &&
            Objects.equals(packingCode, that.packingCode) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(updateBy, that.updateBy) &&
            Objects.equals(deleteAt, that.deleteAt) &&
            Objects.equals(deleteBy, that.deleteBy) &&
            Objects.equals(company, that.company) &&
            Objects.equals(department, that.department) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            unitPrice,
            quantity,
            notes,
            transactionId,
            transactionCode,
            expiredDate,
            manufactureDate,
            itemId,
            itemCode,
            warehouseId,
            warehouseCode,
            supplierId,
            supplierCode,
            customerId,
            customerCode,
            orderId,
            orderCode,
            manufactureId,
            manufactureCode,
            packingId,
            packingCode,
            createAt,
            createBy,
            updateAt,
            updateBy,
            deleteAt,
            deleteBy,
            company,
            department,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionInCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalUnitPrice().map(f -> "unitPrice=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalTransactionId().map(f -> "transactionId=" + f + ", ").orElse("") +
            optionalTransactionCode().map(f -> "transactionCode=" + f + ", ").orElse("") +
            optionalExpiredDate().map(f -> "expiredDate=" + f + ", ").orElse("") +
            optionalManufactureDate().map(f -> "manufactureDate=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalItemCode().map(f -> "itemCode=" + f + ", ").orElse("") +
            optionalWarehouseId().map(f -> "warehouseId=" + f + ", ").orElse("") +
            optionalWarehouseCode().map(f -> "warehouseCode=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalSupplierCode().map(f -> "supplierCode=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalCustomerCode().map(f -> "customerCode=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalOrderCode().map(f -> "orderCode=" + f + ", ").orElse("") +
            optionalManufactureId().map(f -> "manufactureId=" + f + ", ").orElse("") +
            optionalManufactureCode().map(f -> "manufactureCode=" + f + ", ").orElse("") +
            optionalPackingId().map(f -> "packingId=" + f + ", ").orElse("") +
            optionalPackingCode().map(f -> "packingCode=" + f + ", ").orElse("") +
            optionalCreateAt().map(f -> "createAt=" + f + ", ").orElse("") +
            optionalCreateBy().map(f -> "createBy=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalUpdateBy().map(f -> "updateBy=" + f + ", ").orElse("") +
            optionalDeleteAt().map(f -> "deleteAt=" + f + ", ").orElse("") +
            optionalDeleteBy().map(f -> "deleteBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
