package com.masi.logistics.domain.criteria;

import com.masi.logistics.domain.enumeration.StatusEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cglib.core.Local;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.Inventories} entity. This class is used
 * in {@link com.masi.logistics.web.rest.InventoriesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusEntity
     */
    public static class StatusEntityFilter extends Filter<StatusEntity> {

        public StatusEntityFilter() {}

        public StatusEntityFilter(StatusEntityFilter filter) {
            super(filter);
        }

        @Override
        public StatusEntityFilter copy() {
            return new StatusEntityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private UUIDFilter receiverUserId;

    private LocalDateFilter deliveryDate;

    private UUIDFilter inventoriesTypeId;

    private LocalDateFilter dateCreate;

    private UUIDFilter customerId;

    private UUIDFilter customerRecipientId;

    private UUIDFilter invoiceId;

    private StringFilter address;

    private StringFilter note;

    private BigDecimalFilter purchasePrice;

    private BigDecimalFilter salePrice;

    private BigDecimalFilter totalAmount;

    private UUIDFilter inputDepartmentId;

    private UUIDFilter incomingWarehouseId;

    private UUIDFilter outgoingWarehouseId;

    private UUIDFilter employeeId;

    private UUIDFilter orderId;

    private UUIDFilter supplierRequestId;

    private StringFilter taxCode;

    private StringFilter series;

    private StringFilter currencyCodeRate;

    private BigDecimalFilter exchangeRate;

    private BooleanFilter isEmptiness;

    private UUIDFilter emptinessId;

    private StringFilter file;

    private StringFilter attribute;

    private StatusEntityFilter status;

    private BooleanFilter isDeleted;

    private LocalDateFilter createdAt;

    private StringFilter createdBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private StringFilter company;

    private StringFilter department;

    private BooleanFilter isInvoice;

    private StringFilter warehouseGroupType;

    private String search;

    private List<UUID> employeeIds;

    private Boolean distinct;

    public InventoriesCriteria() {}

    public InventoriesCriteria(InventoriesCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.warehouseGroupType = other.optionalWarehouseGroupType().map(StringFilter::copy).orElse(null);
        this.receiverUserId = other.optionalReceiverUserId().map(UUIDFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(LocalDateFilter::copy).orElse(null);
        this.inventoriesTypeId = other.optionalInventoriesTypeId().map(UUIDFilter::copy).orElse(null);
        this.dateCreate = other.optionalDateCreate().map(LocalDateFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.customerRecipientId = other.optionalCustomerRecipientId().map(UUIDFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(UUIDFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.purchasePrice = other.optionalPurchasePrice().map(BigDecimalFilter::copy).orElse(null);
        this.salePrice = other.optionalSalePrice().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.inputDepartmentId = other.optionalInputDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.incomingWarehouseId = other.optionalIncomingWarehouseId().map(UUIDFilter::copy).orElse(null);
        this.outgoingWarehouseId = other.optionalOutgoingWarehouseId().map(UUIDFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(UUIDFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(UUIDFilter::copy).orElse(null);
        this.supplierRequestId = other.optionalSupplierRequestId().map(UUIDFilter::copy).orElse(null);
        this.taxCode = other.optionalTaxCode().map(StringFilter::copy).orElse(null);
        this.series = other.optionalSeries().map(StringFilter::copy).orElse(null);
        this.currencyCodeRate = other.optionalCurrencyCodeRate().map(StringFilter::copy).orElse(null);
        this.exchangeRate = other.optionalExchangeRate().map(BigDecimalFilter::copy).orElse(null);
        this.isEmptiness = other.optionalIsEmptiness().map(BooleanFilter::copy).orElse(null);
        this.emptinessId = other.optionalEmptinessId().map(UUIDFilter::copy).orElse(null);
        this.file = other.optionalFile().map(StringFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StatusEntityFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);

        this.isInvoice = other.optionalIsInvoice().map(BooleanFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.employeeIds = other.optionalEmployeeIds().orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoriesCriteria copy() {
        return new InventoriesCriteria(this);
    }

    public Optional<StringFilter> optionalWarehouseGroupType() {
        return Optional.ofNullable(warehouseGroupType);
    }

    public StringFilter warehouseGroupType() {
        if (warehouseGroupType == null) {
            setWarehouseGroupType(new StringFilter());
        }
        return warehouseGroupType;
    }


    public Optional<BooleanFilter> optionalIsInvoice() {
        return Optional.ofNullable(isInvoice);
    }

    public BooleanFilter isInvoice() {
        if (isInvoice == null) {
            setIsInvoice(new BooleanFilter());
        }
        return isInvoice;
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

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public Optional<UUIDFilter> optionalReceiverUserId() {
        return Optional.ofNullable(receiverUserId);
    }

    public UUIDFilter receiverUserId() {
        if (receiverUserId == null) {
            setReceiverUserId(new UUIDFilter());
        }
        return receiverUserId;
    }

    public Optional<LocalDateFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public LocalDateFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new LocalDateFilter());
        }
        return deliveryDate;
    }

    public Optional<UUIDFilter> optionalInventoriesTypeId() {
        return Optional.ofNullable(inventoriesTypeId);
    }

    public UUIDFilter inventoriesTypeId() {
        if (inventoriesTypeId == null) {
            setInventoriesTypeId(new UUIDFilter());
        }
        return inventoriesTypeId;
    }

    public Optional<LocalDateFilter> optionalDateCreate() {
        return Optional.ofNullable(dateCreate);
    }

    public LocalDateFilter dateCreate() {
        if (dateCreate == null) {
            setDateCreate(new LocalDateFilter());
        }
        return dateCreate;
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

    public Optional<UUIDFilter> optionalCustomerRecipientId() {
        return Optional.ofNullable(customerRecipientId);
    }

    public UUIDFilter customerRecipientId() {
        if (customerRecipientId == null) {
            setCustomerRecipientId(new UUIDFilter());
        }
        return customerRecipientId;
    }

    public Optional<UUIDFilter> optionalInvoiceId() {
        return Optional.ofNullable(invoiceId);
    }

    public UUIDFilter invoiceId() {
        if (invoiceId == null) {
            setInvoiceId(new UUIDFilter());
        }
        return invoiceId;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public Optional<BigDecimalFilter> optionalPurchasePrice() {
        return Optional.ofNullable(purchasePrice);
    }

    public BigDecimalFilter purchasePrice() {
        if (purchasePrice == null) {
            setPurchasePrice(new BigDecimalFilter());
        }
        return purchasePrice;
    }

    public Optional<BigDecimalFilter> optionalSalePrice() {
        return Optional.ofNullable(salePrice);
    }

    public BigDecimalFilter salePrice() {
        if (salePrice == null) {
            setSalePrice(new BigDecimalFilter());
        }
        return salePrice;
    }

    public Optional<BigDecimalFilter> optionalTotalAmount() {
        return Optional.ofNullable(totalAmount);
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            setTotalAmount(new BigDecimalFilter());
        }
        return totalAmount;
    }

    public Optional<UUIDFilter> optionalInputDepartmentId() {
        return Optional.ofNullable(inputDepartmentId);
    }

    public UUIDFilter inputDepartmentId() {
        if (inputDepartmentId == null) {
            setInputDepartmentId(new UUIDFilter());
        }
        return inputDepartmentId;
    }

    public Optional<UUIDFilter> optionalIncomingWarehouseId() {
        return Optional.ofNullable(incomingWarehouseId);
    }

    public UUIDFilter incomingWarehouseId() {
        if (incomingWarehouseId == null) {
            setIncomingWarehouseId(new UUIDFilter());
        }
        return incomingWarehouseId;
    }

    public Optional<UUIDFilter> optionalOutgoingWarehouseId() {
        return Optional.ofNullable(outgoingWarehouseId);
    }

    public UUIDFilter outgoingWarehouseId() {
        if (outgoingWarehouseId == null) {
            setOutgoingWarehouseId(new UUIDFilter());
        }
        return outgoingWarehouseId;
    }

    public Optional<UUIDFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public UUIDFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new UUIDFilter());
        }
        return employeeId;
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

    public Optional<UUIDFilter> optionalSupplierRequestId() {
        return Optional.ofNullable(supplierRequestId);
    }

    public UUIDFilter supplierRequestId() {
        if (supplierRequestId == null) {
            setSupplierRequestId(new UUIDFilter());
        }
        return supplierRequestId;
    }

    public Optional<StringFilter> optionalTaxCode() {
        return Optional.ofNullable(taxCode);
    }

    public StringFilter taxCode() {
        if (taxCode == null) {
            setTaxCode(new StringFilter());
        }
        return taxCode;
    }

    public Optional<StringFilter> optionalSeries() {
        return Optional.ofNullable(series);
    }

    public StringFilter series() {
        if (series == null) {
            setSeries(new StringFilter());
        }
        return series;
    }

    public Optional<StringFilter> optionalCurrencyCodeRate() {
        return Optional.ofNullable(currencyCodeRate);
    }

    public StringFilter currencyCodeRate() {
        if (currencyCodeRate == null) {
            setCurrencyCodeRate(new StringFilter());
        }
        return currencyCodeRate;
    }

    public Optional<BigDecimalFilter> optionalExchangeRate() {
        return Optional.ofNullable(exchangeRate);
    }

    public BigDecimalFilter exchangeRate() {
        if (exchangeRate == null) {
            setExchangeRate(new BigDecimalFilter());
        }
        return exchangeRate;
    }

    public Optional<BooleanFilter> optionalIsEmptiness() {
        return Optional.ofNullable(isEmptiness);
    }

    public BooleanFilter isEmptiness() {
        if (isEmptiness == null) {
            setIsEmptiness(new BooleanFilter());
        }
        return isEmptiness;
    }

    public Optional<UUIDFilter> optionalEmptinessId() {
        return Optional.ofNullable(emptinessId);
    }

    public UUIDFilter emptinessId() {
        if (emptinessId == null) {
            setEmptinessId(new UUIDFilter());
        }
        return emptinessId;
    }

    public Optional<StringFilter> optionalFile() {
        return Optional.ofNullable(file);
    }

    public StringFilter file() {
        if (file == null) {
            setFile(new StringFilter());
        }
        return file;
    }

    public Optional<StringFilter> optionalAttribute() {
        return Optional.ofNullable(attribute);
    }

    public StringFilter attribute() {
        if (attribute == null) {
            setAttribute(new StringFilter());
        }
        return attribute;
    }

    public Optional<StatusEntityFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StatusEntityFilter status() {
        if (status == null) {
            setStatus(new StatusEntityFilter());
        }
        return status;
    }

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public Optional<StringFilter> optionalUpdatedBy() {
        return Optional.ofNullable(updatedBy);
    }

    public StringFilter updatedBy() {
        if (updatedBy == null) {
            setUpdatedBy(new StringFilter());
        }
        return updatedBy;
    }

    public Optional<ZonedDateTimeFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public ZonedDateTimeFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new ZonedDateTimeFilter());
        }
        return deletedAt;
    }

    public Optional<StringFilter> optionalDeletedBy() {
        return Optional.ofNullable(deletedBy);
    }

    public StringFilter deletedBy() {
        if (deletedBy == null) {
            setDeletedBy(new StringFilter());
        }
        return deletedBy;
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

    public Optional<StringFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public StringFilter department() {
        if (department == null) {
            setDepartment(new StringFilter());
        }
        return department;
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

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new LocalDateFilter());
        }
        return createdAt;
    }

    public Optional<String> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public String search() {
        if (search == null) {
            setSearch(null);
        }
        return search;
    }

    public Optional<List<UUID>> optionalEmployeeIds() {
        return Optional.ofNullable(employeeIds);
    }

    public List<UUID> employeeIds() {
        if (employeeIds == null) {
            setEmployeeIds(null);
        }
        return employeeIds;
    }

}
