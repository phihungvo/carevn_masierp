package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.PaymentDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.PaymentDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter paymentRequestId;

    private UUIDFilter invoiceId;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private Boolean distinct;

    public PaymentDetailCriteria() {}

    public PaymentDetailCriteria(PaymentDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.paymentRequestId = other.optionalPaymentRequestId().map(UUIDFilter::copy).orElse(null);
        this.invoiceId = other.optionalInvoiceId().map(UUIDFilter::copy).orElse(null);
        this.paymentRequestId = other.optionalPaymentRequestId().map(UUIDFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentDetailCriteria copy() {
        return new PaymentDetailCriteria(this);
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

    public Optional<UUIDFilter> optionalPaymentRequestId() {
        return Optional.ofNullable(paymentRequestId);
    }

    public UUIDFilter paymentRequestId() {
        if (paymentRequestId == null) {
            setPaymentRequestId(new UUIDFilter());
        }
        return paymentRequestId;
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

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
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

}
