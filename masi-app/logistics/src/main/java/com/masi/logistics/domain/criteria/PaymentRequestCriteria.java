package com.masi.logistics.domain.criteria;

import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.PaymentRequest} entity. This class is used
 * in {@link com.masi.logistics.web.rest.PaymentRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RequestTypeEnum
     */
    public static class RequestTypeEnumFilter extends Filter<RequestTypeEnum> {

        public RequestTypeEnumFilter() {
        }

        public RequestTypeEnumFilter(RequestTypeEnumFilter filter) {
            super(filter);
        }

        @Override
        public RequestTypeEnumFilter copy() {
            return new RequestTypeEnumFilter(this);
        }
    }

    /**
     * Class for filtering RequestStatus
     */
    public static class RequestStatusFilter extends Filter<RequestStatus> {

        public RequestStatusFilter() {
        }

        public RequestStatusFilter(RequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public RequestStatusFilter copy() {
            return new RequestStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter employeeId;

    private IntegerFilter order;

    private UUIDFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private RequestTypeEnumFilter type;

    private UUIDFilter departmentId;

    private StringFilter company;

    private StringFilter content;

    private StringFilter attachments;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter paidAmount;

    private BigDecimalFilter remainingAmount;

    private ZonedDateTimeFilter paymentDate;

    private StringFilter note;

    private RequestStatusFilter status;

    private UUIDFilter supplierId;

    private String search;

    private List<UUID> employeeIds;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private Boolean distinct;

    public PaymentRequestCriteria() {
    }

    public PaymentRequestCriteria(PaymentRequestCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(StringFilter::copy).orElse(null);
        this.order = other.optionalOrder().map(IntegerFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(UUIDFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.type = other.optionalType().map(RequestTypeEnumFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.attachments = other.optionalAttachments().map(StringFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paidAmount = other.optionalPaidAmount().map(BigDecimalFilter::copy).orElse(null);
        this.remainingAmount = other.optionalRemainingAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(RequestStatusFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.employeeIds = other.optionalEmployeeIds().orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);

        this.distinct = other.distinct;
    }

    @Override
    public PaymentRequestCriteria copy() {
        return new PaymentRequestCriteria(this);
    }

    public Optional<StringFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public StringFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new StringFilter());
        }
        return employeeId;
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

    public Optional<IntegerFilter> optionalOrder() {
        return Optional.ofNullable(order);
    }

    public IntegerFilter order() {
        if (order == null) {
            setOrder(new IntegerFilter());
        }
        return order;
    }

    public Optional<UUIDFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public UUIDFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new UUIDFilter());
        }
        return createdBy;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
    }

    public Optional<RequestTypeEnumFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public RequestTypeEnumFilter type() {
        if (type == null) {
            setType(new RequestTypeEnumFilter());
        }
        return type;
    }

    public Optional<UUIDFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public UUIDFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new UUIDFilter());
        }
        return departmentId;
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

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public Optional<StringFilter> optionalAttachments() {
        return Optional.ofNullable(attachments);
    }

    public StringFilter attachments() {
        if (attachments == null) {
            setAttachments(new StringFilter());
        }
        return attachments;
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

    public Optional<BigDecimalFilter> optionalPaidAmount() {
        return Optional.ofNullable(paidAmount);
    }

    public BigDecimalFilter paidAmount() {
        if (paidAmount == null) {
            setPaidAmount(new BigDecimalFilter());
        }
        return paidAmount;
    }

    public Optional<BigDecimalFilter> optionalRemainingAmount() {
        return Optional.ofNullable(remainingAmount);
    }

    public BigDecimalFilter remainingAmount() {
        if (remainingAmount == null) {
            setRemainingAmount(new BigDecimalFilter());
        }
        return remainingAmount;
    }

    public Optional<ZonedDateTimeFilter> optionalPaymentDate() {
        return Optional.ofNullable(paymentDate);
    }

    public ZonedDateTimeFilter paymentDate() {
        if (paymentDate == null) {
            setPaymentDate(new ZonedDateTimeFilter());
        }
        return paymentDate;
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

    public Optional<RequestStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public RequestStatusFilter status() {
        if (status == null) {
            setStatus(new RequestStatusFilter());
        }
        return status;
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

    public Optional<UUIDFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public UUIDFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new UUIDFilter());
        }
        return supplierId;
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

}
