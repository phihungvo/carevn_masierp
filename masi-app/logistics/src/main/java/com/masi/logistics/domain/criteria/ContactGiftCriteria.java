package com.masi.logistics.domain.criteria;

import com.masi.logistics.domain.enumeration.ContactGiftStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.ContactGift} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ContactGiftResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-gifts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactGiftCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContactGiftStatus
     */
    public static class ContactGiftStatusFilter extends Filter<ContactGiftStatus> {

        public ContactGiftStatusFilter() {}

        public ContactGiftStatusFilter(ContactGiftStatusFilter filter) {
            super(filter);
        }

        @Override
        public ContactGiftStatusFilter copy() {
            return new ContactGiftStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter giftName;

    private IntegerFilter value;

    private BooleanFilter isGiving;

    private ContactGiftStatusFilter status;

    private ZonedDateTimeFilter expectedDate;

    private ZonedDateTimeFilter dateOfGiving;

    private StringFilter company;

    private StringFilter department;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private UUIDFilter contactId;

    private Boolean distinct;

    public ContactGiftCriteria() {}

    public ContactGiftCriteria(ContactGiftCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.giftName = other.optionalGiftName().map(StringFilter::copy).orElse(null);
        this.value = other.optionalValue().map(IntegerFilter::copy).orElse(null);
        this.isGiving = other.optionalIsGiving().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ContactGiftStatusFilter::copy).orElse(null);
        this.expectedDate = other.optionalExpectedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.dateOfGiving = other.optionalDateOfGiving().map(ZonedDateTimeFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.contactId = other.optionalContactId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContactGiftCriteria copy() {
        return new ContactGiftCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getGiftName() {
        return giftName;
    }

    public Optional<StringFilter> optionalGiftName() {
        return Optional.ofNullable(giftName);
    }

    public StringFilter giftName() {
        if (giftName == null) {
            setGiftName(new StringFilter());
        }
        return giftName;
    }

    public void setGiftName(StringFilter giftName) {
        this.giftName = giftName;
    }

    public IntegerFilter getValue() {
        return value;
    }

    public Optional<IntegerFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public IntegerFilter value() {
        if (value == null) {
            setValue(new IntegerFilter());
        }
        return value;
    }

    public void setValue(IntegerFilter value) {
        this.value = value;
    }

    public BooleanFilter getIsGiving() {
        return isGiving;
    }

    public Optional<BooleanFilter> optionalIsGiving() {
        return Optional.ofNullable(isGiving);
    }

    public BooleanFilter isGiving() {
        if (isGiving == null) {
            setIsGiving(new BooleanFilter());
        }
        return isGiving;
    }

    public void setIsGiving(BooleanFilter isGiving) {
        this.isGiving = isGiving;
    }

    public ContactGiftStatusFilter getStatus() {
        return status;
    }

    public Optional<ContactGiftStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ContactGiftStatusFilter status() {
        if (status == null) {
            setStatus(new ContactGiftStatusFilter());
        }
        return status;
    }

    public void setStatus(ContactGiftStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getExpectedDate() {
        return expectedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalExpectedDate() {
        return Optional.ofNullable(expectedDate);
    }

    public ZonedDateTimeFilter expectedDate() {
        if (expectedDate == null) {
            setExpectedDate(new ZonedDateTimeFilter());
        }
        return expectedDate;
    }

    public void setExpectedDate(ZonedDateTimeFilter expectedDate) {
        this.expectedDate = expectedDate;
    }

    public ZonedDateTimeFilter getDateOfGiving() {
        return dateOfGiving;
    }

    public Optional<ZonedDateTimeFilter> optionalDateOfGiving() {
        return Optional.ofNullable(dateOfGiving);
    }

    public ZonedDateTimeFilter dateOfGiving() {
        if (dateOfGiving == null) {
            setDateOfGiving(new ZonedDateTimeFilter());
        }
        return dateOfGiving;
    }

    public void setDateOfGiving(ZonedDateTimeFilter dateOfGiving) {
        this.dateOfGiving = dateOfGiving;
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

    public UUIDFilter getContactId() {
        return contactId;
    }

    public Optional<UUIDFilter> optionalContactId() {
        return Optional.ofNullable(contactId);
    }

    public UUIDFilter contactId() {
        if (contactId == null) {
            setContactId(new UUIDFilter());
        }
        return contactId;
    }

    public void setContactId(UUIDFilter contactId) {
        this.contactId = contactId;
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
        final ContactGiftCriteria that = (ContactGiftCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(giftName, that.giftName) &&
            Objects.equals(value, that.value) &&
            Objects.equals(isGiving, that.isGiving) &&
            Objects.equals(status, that.status) &&
            Objects.equals(expectedDate, that.expectedDate) &&
            Objects.equals(dateOfGiving, that.dateOfGiving) &&
            Objects.equals(company, that.company) &&
            Objects.equals(department, that.department) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(deletedBy, that.deletedBy) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            giftName,
            value,
            isGiving,
            status,
            expectedDate,
            dateOfGiving,
            company,
            department,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            deletedBy,
            deletedAt,
            contactId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGiftCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalGiftName().map(f -> "giftName=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalIsGiving().map(f -> "isGiving=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalExpectedDate().map(f -> "expectedDate=" + f + ", ").orElse("") +
            optionalDateOfGiving().map(f -> "dateOfGiving=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDeletedBy().map(f -> "deletedBy=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalContactId().map(f -> "contactId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
