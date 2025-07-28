package com.masi.logistics.domain.criteria;

import com.masi.logistics.domain.enumeration.RequestStatus;
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
 * Criteria class for the {@link com.masi.logistics.domain.SuppliesRequest} entity. This class is used
 * in {@link com.masi.logistics.web.rest.SuppliesRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplies-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RequestStatus
     */
    public static class RequestStatusFilter extends Filter<RequestStatus> {

        public RequestStatusFilter() {}

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

    private StringFilter requestNumber;

    private LocalDateFilter requestDate;

    private UUIDFilter requestByEmployeeId;

    private UUIDFilter departmentId;

    private RequestStatusFilter requestStatus;

    private BigDecimalFilter totalAmount;

    private StringFilter note;

    private StringFilter company;

    private StringFilter department;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private UUIDFilter requestTypeId;

    private String search;

    private List<UUID> employeeIds;

    private Boolean distinct;

    public SuppliesRequestCriteria() {}

    public SuppliesRequestCriteria(SuppliesRequestCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.requestNumber = other.optionalRequestNumber().map(StringFilter::copy).orElse(null);
        this.requestDate = other.optionalRequestDate().map(LocalDateFilter::copy).orElse(null);
        this.requestByEmployeeId = other.optionalRequestByEmployeeId().map(UUIDFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.requestStatus = other.optionalRequestStatus().map(RequestStatusFilter::copy).orElse(null);
        this.totalAmount = other.optionalTotalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.employeeIds = other.optionalEmployeeIds().orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SuppliesRequestCriteria copy() {
        return new SuppliesRequestCriteria(this);
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

    public Optional<StringFilter> optionalRequestNumber() {
        return Optional.ofNullable(requestNumber);
    }

    public StringFilter requestNumber() {
        if (requestNumber == null) {
            setRequestNumber(new StringFilter());
        }
        return requestNumber;
    }

    public Optional<LocalDateFilter> optionalRequestDate() {
        return Optional.ofNullable(requestDate);
    }

    public LocalDateFilter requestDate() {
        if (requestDate == null) {
            setRequestDate(new LocalDateFilter());
        }
        return requestDate;
    }

    public Optional<UUIDFilter> optionalRequestByEmployeeId() {
        return Optional.ofNullable(requestByEmployeeId);
    }

    public UUIDFilter requestByEmployeeId() {
        if (requestByEmployeeId == null) {
            setRequestByEmployeeId(new UUIDFilter());
        }
        return requestByEmployeeId;
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

    public Optional<RequestStatusFilter> optionalRequestStatus() {
        return Optional.ofNullable(requestStatus);
    }

    public RequestStatusFilter requestStatus() {
        if (requestStatus == null) {
            setRequestStatus(new RequestStatusFilter());
        }
        return requestStatus;
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

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
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

    public Optional<String> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public String search() {
        if (search == null) {
            setSearch("");
        }
        return search;
    }

    public Optional<List<UUID>> optionalEmployeeIds() {
        return Optional.ofNullable(employeeIds);
    }

    public List<UUID> employeeIds() {
        if (employeeIds == null) {
            setEmployeeIds(List.of());
        }
        return employeeIds;
    }

    public Optional<UUIDFilter> optionalRequestTypeId() {
        return Optional.ofNullable(requestTypeId);
    }

    public UUIDFilter requestTypeId() {
        if (requestTypeId == null) {
            setRequestTypeId(new UUIDFilter());
        }
        return requestTypeId;
    }

}
