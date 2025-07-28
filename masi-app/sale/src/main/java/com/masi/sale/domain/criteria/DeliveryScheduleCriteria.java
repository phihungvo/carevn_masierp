package com.masi.sale.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.sale.domain.DeliverySchedule} entity. This class is used
 * in {@link com.masi.sale.web.rest.DeliveryScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delivery-schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryScheduleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private LocalDateFilter deliveryDate;

    private LocalDateFilter expectedReceiveDate;

    private UUIDFilter contractId;

    private UUIDFilter orderId;

    private StringFilter content;

    private IntegerFilter quantity;

    private UUIDFilter unitId;

    private BigDecimalFilter price;

    private BigDecimalFilter total;

    private StringFilter paymentMethod;

    private StringFilter receiverName;

    private StringFilter deliveryLocation;

    private StringFilter note;

    private StringFilter type;

    private StringFilter attachment;

    private StringFilter attribute;

    private BooleanFilter isDeleted;

    private ZonedDateTimeFilter createdAt;

    private StringFilter createdBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private StringFilter company;

    private StringFilter department;

    private Boolean distinct;

    public DeliveryScheduleCriteria() {}

    public DeliveryScheduleCriteria(DeliveryScheduleCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(LocalDateFilter::copy).orElse(null);
        this.expectedReceiveDate = other.optionalExpectedReceiveDate().map(LocalDateFilter::copy).orElse(null);
        this.contractId = other.optionalContractId().map(UUIDFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(UUIDFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.unitId = other.optionalUnitId().map(UUIDFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(StringFilter::copy).orElse(null);
        this.receiverName = other.optionalReceiverName().map(StringFilter::copy).orElse(null);
        this.deliveryLocation = other.optionalDeliveryLocation().map(StringFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.attachment = other.optionalAttachment().map(StringFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DeliveryScheduleCriteria copy() {
        return new DeliveryScheduleCriteria(this);
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

    public LocalDateFilter getDeliveryDate() {
        return deliveryDate;
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

    public void setDeliveryDate(LocalDateFilter deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDateFilter getExpectedReceiveDate() {
        return expectedReceiveDate;
    }

    public Optional<LocalDateFilter> optionalExpectedReceiveDate() {
        return Optional.ofNullable(expectedReceiveDate);
    }

    public LocalDateFilter expectedReceiveDate() {
        if (expectedReceiveDate == null) {
            setExpectedReceiveDate(new LocalDateFilter());
        }
        return expectedReceiveDate;
    }

    public void setExpectedReceiveDate(LocalDateFilter expectedReceiveDate) {
        this.expectedReceiveDate = expectedReceiveDate;
    }

    public UUIDFilter getContractId() {
        return contractId;
    }

    public Optional<UUIDFilter> optionalContractId() {
        return Optional.ofNullable(contractId);
    }

    public UUIDFilter contractId() {
        if (contractId == null) {
            setContractId(new UUIDFilter());
        }
        return contractId;
    }

    public void setContractId(UUIDFilter contractId) {
        this.contractId = contractId;
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

    public StringFilter getContent() {
        return content;
    }

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
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

    public UUIDFilter getUnitId() {
        return unitId;
    }

    public Optional<UUIDFilter> optionalUnitId() {
        return Optional.ofNullable(unitId);
    }

    public UUIDFilter unitId() {
        if (unitId == null) {
            setUnitId(new UUIDFilter());
        }
        return unitId;
    }

    public void setUnitId(UUIDFilter unitId) {
        this.unitId = unitId;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public Optional<BigDecimalFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public BigDecimalFilter total() {
        if (total == null) {
            setTotal(new BigDecimalFilter());
        }
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<StringFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new StringFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getReceiverName() {
        return receiverName;
    }

    public Optional<StringFilter> optionalReceiverName() {
        return Optional.ofNullable(receiverName);
    }

    public StringFilter receiverName() {
        if (receiverName == null) {
            setReceiverName(new StringFilter());
        }
        return receiverName;
    }

    public void setReceiverName(StringFilter receiverName) {
        this.receiverName = receiverName;
    }

    public StringFilter getDeliveryLocation() {
        return deliveryLocation;
    }

    public Optional<StringFilter> optionalDeliveryLocation() {
        return Optional.ofNullable(deliveryLocation);
    }

    public StringFilter deliveryLocation() {
        if (deliveryLocation == null) {
            setDeliveryLocation(new StringFilter());
        }
        return deliveryLocation;
    }

    public void setDeliveryLocation(StringFilter deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public StringFilter getNote() {
        return note;
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

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getAttachment() {
        return attachment;
    }

    public Optional<StringFilter> optionalAttachment() {
        return Optional.ofNullable(attachment);
    }

    public StringFilter attachment() {
        if (attachment == null) {
            setAttachment(new StringFilter());
        }
        return attachment;
    }

    public void setAttachment(StringFilter attachment) {
        this.attachment = attachment;
    }

    public StringFilter getAttribute() {
        return attribute;
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

    public void setAttribute(StringFilter attribute) {
        this.attribute = attribute;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
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

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
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

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
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

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StringFilter getUpdatedBy() {
        return updatedBy;
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

    public void setUpdatedBy(StringFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTimeFilter getDeletedAt() {
        return deletedAt;
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

    public void setDeletedAt(ZonedDateTimeFilter deletedAt) {
        this.deletedAt = deletedAt;
    }

    public StringFilter getDeletedBy() {
        return deletedBy;
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

    public void setDeletedBy(StringFilter deletedBy) {
        this.deletedBy = deletedBy;
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
        final DeliveryScheduleCriteria that = (DeliveryScheduleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(deliveryDate, that.deliveryDate) &&
            Objects.equals(expectedReceiveDate, that.expectedReceiveDate) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(content, that.content) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(price, that.price) &&
            Objects.equals(total, that.total) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(receiverName, that.receiverName) &&
            Objects.equals(deliveryLocation, that.deliveryLocation) &&
            Objects.equals(note, that.note) &&
            Objects.equals(type, that.type) &&
            Objects.equals(attachment, that.attachment) &&
            Objects.equals(attribute, that.attribute) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(deletedBy, that.deletedBy) &&
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
            deliveryDate,
            expectedReceiveDate,
            contractId,
            orderId,
            content,
            quantity,
            unitId,
            price,
            total,
            paymentMethod,
            receiverName,
            deliveryLocation,
            note,
            type,
            attachment,
            attribute,
            isDeleted,
            createdAt,
            createdBy,
            updatedAt,
            updatedBy,
            deletedAt,
            deletedBy,
            company,
            department,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryScheduleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDeliveryDate().map(f -> "deliveryDate=" + f + ", ").orElse("") +
            optionalExpectedReceiveDate().map(f -> "expectedReceiveDate=" + f + ", ").orElse("") +
            optionalContractId().map(f -> "contractId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalContent().map(f -> "content=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalUnitId().map(f -> "unitId=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalReceiverName().map(f -> "receiverName=" + f + ", ").orElse("") +
            optionalDeliveryLocation().map(f -> "deliveryLocation=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalAttachment().map(f -> "attachment=" + f + ", ").orElse("") +
            optionalAttribute().map(f -> "attribute=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalDeletedBy().map(f -> "deletedBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
