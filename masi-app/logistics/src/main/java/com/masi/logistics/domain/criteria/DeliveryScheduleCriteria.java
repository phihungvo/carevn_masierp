package com.masi.logistics.domain.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import com.masi.logistics.domain.DeliverySchedule;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.DeliverySchedule} entity. This class is used
 * in {@link com.masi.logistics.web.rest.DeliveryScheduleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delivery-schedules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Setter
@Getter
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryScheduleCriteria implements Serializable, Criteria {

    public static class StatusFilter extends Filter<DeliverySchedule.Status> {

        public StatusFilter() {
        }

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }
    @Serial
    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private LocalDateFilter deliveryDate;

    private LocalDateFilter expectedReceiveDate;

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

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private Boolean distinct;
    private StatusFilter status;
    public DeliveryScheduleCriteria status(StatusFilter status) {
        this.status = status;
        return this;
    }
    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public DeliveryScheduleCriteria() {}

    public DeliveryScheduleCriteria(DeliveryScheduleCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(LocalDateFilter::copy).orElse(null);
        this.expectedReceiveDate = other.optionalExpectedReceiveDate().map(LocalDateFilter::copy).orElse(null);
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
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.status == null ? null : other.status.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeliveryScheduleCriteria copy() {
        return new DeliveryScheduleCriteria(this);
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

    public Optional<LocalDateFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public LocalDateFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new LocalDateFilter());
        }
        return deliveryDate;
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

    public Optional<UUIDFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public UUIDFilter orderId() {
        if (orderId == null) {
            setOrderId(new UUIDFilter());
        }
        return orderId;
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

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
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

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
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

    public Optional<StringFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new StringFilter());
        }
        return paymentMethod;
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

    public Optional<StringFilter> optionalDeliveryLocation() {
        return Optional.ofNullable(deliveryLocation);
    }

    public StringFilter deliveryLocation() {
        if (deliveryLocation == null) {
            setDeliveryLocation(new StringFilter());
        }
        return deliveryLocation;
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

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
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

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
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

    public Optional<ZonedDateTimeFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public ZonedDateTimeFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new ZonedDateTimeFilter());
        }
        return deletedAt;
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
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(deletedBy, that.deletedBy) &&
            Objects.equals(deletedAt, that.deletedAt) &&
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
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            deletedBy,
            deletedAt,
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
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDeletedBy().map(f -> "deletedBy=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
