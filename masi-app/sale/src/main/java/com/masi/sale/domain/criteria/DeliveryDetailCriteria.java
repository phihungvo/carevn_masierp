package com.masi.sale.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.sale.domain.DeliveryDetail} entity. This class is used
 * in {@link com.masi.sale.web.rest.DeliveryDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delivery-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter deliveryId;

    private UUIDFilter contractMaterialId;

    private IntegerFilter quantity;

    private UUIDFilter uomId;

    private BigDecimalFilter price;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter createdBy;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private StringFilter attachment;

    private Boolean distinct;

    private ZonedDateTimeFilter deliveryDate;

    private StringFilter company;

    private UUIDFilter orderId;

    public DeliveryDetailCriteria() {}

    public DeliveryDetailCriteria(DeliveryDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.deliveryId = other.optionalDeliveryId().map(UUIDFilter::copy).orElse(null);
        this.contractMaterialId = other.optionalContractMaterialId().map(UUIDFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(UUIDFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.attachment = other.optionalAttachment().map(StringFilter::copy).orElse(null);
        this.deliveryDate = other.optionalDeliveryDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(UUIDFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DeliveryDetailCriteria copy() {
        return new DeliveryDetailCriteria(this);
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

    public Optional<UUIDFilter> optionalDeliveryId() {
        return Optional.ofNullable(deliveryId);
    }

    public UUIDFilter deliveryId() {
        if (deliveryId == null) {
            setDeliveryId(new UUIDFilter());
        }
        return deliveryId;
    }

    public Optional<UUIDFilter> optionalContractMaterialId() {
        return Optional.ofNullable(contractMaterialId);
    }

    public UUIDFilter contractMaterialId() {
        if (contractMaterialId == null) {
            setContractMaterialId(new UUIDFilter());
        }
        return contractMaterialId;
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

    public Optional<UUIDFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public UUIDFilter uomId() {
        if (uomId == null) {
            setUomId(new UUIDFilter());
        }
        return uomId;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
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

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
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

    public Optional<StringFilter> optionalAttachment() {
        return Optional.ofNullable(attachment);
    }

    public StringFilter attachment() {
        if (attachment == null) {
            setAttachment(new StringFilter());
        }
        return attachment;
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

    public Optional<ZonedDateTimeFilter> optionalDeliveryDate() {
        return Optional.ofNullable(deliveryDate);
    }

    public ZonedDateTimeFilter deliveryDate() {
        if (deliveryDate == null) {
            setDeliveryDate(new ZonedDateTimeFilter());
        }
        return deliveryDate;
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

    public Optional<StringFilter> optionalCompany() {
        return Optional.ofNullable(company);
    }

    public StringFilter company() {
        if (company == null) {
            setCompany(new StringFilter());
        }
        return company;
    }
}
