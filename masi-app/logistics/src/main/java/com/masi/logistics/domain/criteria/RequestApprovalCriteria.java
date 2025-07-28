package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.RequestApproval} entity. This class is used
 * in {@link com.masi.logistics.web.rest.RequestApprovalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /request-approvals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestApprovalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private IntegerFilter index;

    private StringFilter documentId;

    private StringFilter employeeId;

    private BooleanFilter result;

    private StringFilter approvedSign;

    private StringFilter approvedSignName;

    private StringFilter rejectNote;

    private StringFilter company;

    private StringFilter department;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private Boolean distinct;

    public RequestApprovalCriteria() {}

    public RequestApprovalCriteria(RequestApprovalCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.index = other.optionalIndex().map(IntegerFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(StringFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(StringFilter::copy).orElse(null);
        this.result = other.optionalResult().map(BooleanFilter::copy).orElse(null);
        this.approvedSign = other.optionalApprovedSign().map(StringFilter::copy).orElse(null);
        this.approvedSignName = other.optionalApprovedSignName().map(StringFilter::copy).orElse(null);
        this.rejectNote = other.optionalRejectNote().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RequestApprovalCriteria copy() {
        return new RequestApprovalCriteria(this);
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

    public IntegerFilter getIndex() {
        return index;
    }

    public Optional<IntegerFilter> optionalIndex() {
        return Optional.ofNullable(index);
    }

    public IntegerFilter index() {
        if (index == null) {
            setIndex(new IntegerFilter());
        }
        return index;
    }

    public void setIndex(IntegerFilter index) {
        this.index = index;
    }

    public StringFilter getDocumentId() {
        return documentId;
    }

    public Optional<StringFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public StringFilter documentId() {
        if (documentId == null) {
            setDocumentId(new StringFilter());
        }
        return documentId;
    }

    public void setDocumentId(StringFilter documentId) {
        this.documentId = documentId;
    }

    public StringFilter getEmployeeId() {
        return employeeId;
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

    public void setEmployeeId(StringFilter employeeId) {
        this.employeeId = employeeId;
    }

    public BooleanFilter getResult() {
        return result;
    }

    public Optional<BooleanFilter> optionalResult() {
        return Optional.ofNullable(result);
    }

    public BooleanFilter result() {
        if (result == null) {
            setResult(new BooleanFilter());
        }
        return result;
    }

    public void setResult(BooleanFilter result) {
        this.result = result;
    }

    public StringFilter getApprovedSign() {
        return approvedSign;
    }

    public Optional<StringFilter> optionalApprovedSign() {
        return Optional.ofNullable(approvedSign);
    }

    public StringFilter approvedSign() {
        if (approvedSign == null) {
            setApprovedSign(new StringFilter());
        }
        return approvedSign;
    }

    public void setApprovedSign(StringFilter approvedSign) {
        this.approvedSign = approvedSign;
    }

    public StringFilter getApprovedSignName() {
        return approvedSignName;
    }

    public Optional<StringFilter> optionalApprovedSignName() {
        return Optional.ofNullable(approvedSignName);
    }

    public StringFilter approvedSignName() {
        if (approvedSignName == null) {
            setApprovedSignName(new StringFilter());
        }
        return approvedSignName;
    }

    public void setApprovedSignName(StringFilter approvedSignName) {
        this.approvedSignName = approvedSignName;
    }

    public StringFilter getRejectNote() {
        return rejectNote;
    }

    public Optional<StringFilter> optionalRejectNote() {
        return Optional.ofNullable(rejectNote);
    }

    public StringFilter rejectNote() {
        if (rejectNote == null) {
            setRejectNote(new StringFilter());
        }
        return rejectNote;
    }

    public void setRejectNote(StringFilter rejectNote) {
        this.rejectNote = rejectNote;
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

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
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

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
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
        final RequestApprovalCriteria that = (RequestApprovalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(index, that.index) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(result, that.result) &&
            Objects.equals(approvedSign, that.approvedSign) &&
            Objects.equals(approvedSignName, that.approvedSignName) &&
            Objects.equals(rejectNote, that.rejectNote) &&
            Objects.equals(company, that.company) &&
            Objects.equals(department, that.department) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
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
            index,
            documentId,
            employeeId,
            result,
            approvedSign,
            approvedSignName,
            rejectNote,
            company,
            department,
            isDeleted,
            createdBy,
            createdDate,
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
        return "RequestApprovalCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndex().map(f -> "index=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalResult().map(f -> "result=" + f + ", ").orElse("") +
            optionalApprovedSign().map(f -> "approvedSign=" + f + ", ").orElse("") +
            optionalApprovedSignName().map(f -> "approvedSignName=" + f + ", ").orElse("") +
            optionalRejectNote().map(f -> "rejectNote=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDeletedBy().map(f -> "deletedBy=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
