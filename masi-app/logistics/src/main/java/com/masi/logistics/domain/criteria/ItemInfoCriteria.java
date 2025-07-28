package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.ItemInfo} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ItemInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter registrationNumber;

    private ZonedDateTimeFilter registrationDate;

    private StringFilter handoverNumber;

    private ZonedDateTimeFilter handoverDate;

    private StringFilter handoverBy;

    private UUIDFilter userId;

    private StringFilter userPosition;

    private StringFilter seriesNumber;

    private ZonedDateTimeFilter usageDate;

    private StringFilter invoiceNumber;

    private ZonedDateTimeFilter invoiceDate;

    private StringFilter status;

    private ZonedDateTimeFilter liquidationDate;

    private UUIDFilter unit;

    private IntegerFilter yearOfUse;

    private IntegerFilter monthOfUse;

    private FloatFilter warrantyPeriod;

    private StringFilter manufacturer;

    private BooleanFilter isMadeIn;

    private StringFilter specs;

    private ZonedDateTimeFilter removalDate;

    private StringFilter reasonForRemoval;

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

    public ItemInfoCriteria() {}

    public ItemInfoCriteria(ItemInfoCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.registrationNumber = other.optionalRegistrationNumber().map(StringFilter::copy).orElse(null);
        this.registrationDate = other.optionalRegistrationDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.handoverNumber = other.optionalHandoverNumber().map(StringFilter::copy).orElse(null);
        this.handoverDate = other.optionalHandoverDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.handoverBy = other.optionalHandoverBy().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(UUIDFilter::copy).orElse(null);
        this.userPosition = other.optionalUserPosition().map(StringFilter::copy).orElse(null);
        this.seriesNumber = other.optionalSeriesNumber().map(StringFilter::copy).orElse(null);
        this.usageDate = other.optionalUsageDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.invoiceNumber = other.optionalInvoiceNumber().map(StringFilter::copy).orElse(null);
        this.invoiceDate = other.optionalInvoiceDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.liquidationDate = other.optionalLiquidationDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.unit = other.optionalUnit().map(UUIDFilter::copy).orElse(null);
        this.yearOfUse = other.optionalYearOfUse().map(IntegerFilter::copy).orElse(null);
        this.monthOfUse = other.optionalMonthOfUse().map(IntegerFilter::copy).orElse(null);
        this.warrantyPeriod = other.optionalWarrantyPeriod().map(FloatFilter::copy).orElse(null);
        this.manufacturer = other.optionalManufacturer().map(StringFilter::copy).orElse(null);
        this.isMadeIn = other.optionalIsMadeIn().map(BooleanFilter::copy).orElse(null);
        this.specs = other.optionalSpecs().map(StringFilter::copy).orElse(null);
        this.removalDate = other.optionalRemovalDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.reasonForRemoval = other.optionalReasonForRemoval().map(StringFilter::copy).orElse(null);
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
    public ItemInfoCriteria copy() {
        return new ItemInfoCriteria(this);
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

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public Optional<StringFilter> optionalRegistrationNumber() {
        return Optional.ofNullable(registrationNumber);
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            setRegistrationNumber(new StringFilter());
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ZonedDateTimeFilter getRegistrationDate() {
        return registrationDate;
    }

    public Optional<ZonedDateTimeFilter> optionalRegistrationDate() {
        return Optional.ofNullable(registrationDate);
    }

    public ZonedDateTimeFilter registrationDate() {
        if (registrationDate == null) {
            setRegistrationDate(new ZonedDateTimeFilter());
        }
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTimeFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public StringFilter getHandoverNumber() {
        return handoverNumber;
    }

    public Optional<StringFilter> optionalHandoverNumber() {
        return Optional.ofNullable(handoverNumber);
    }

    public StringFilter handoverNumber() {
        if (handoverNumber == null) {
            setHandoverNumber(new StringFilter());
        }
        return handoverNumber;
    }

    public void setHandoverNumber(StringFilter handoverNumber) {
        this.handoverNumber = handoverNumber;
    }

    public ZonedDateTimeFilter getHandoverDate() {
        return handoverDate;
    }

    public Optional<ZonedDateTimeFilter> optionalHandoverDate() {
        return Optional.ofNullable(handoverDate);
    }

    public ZonedDateTimeFilter handoverDate() {
        if (handoverDate == null) {
            setHandoverDate(new ZonedDateTimeFilter());
        }
        return handoverDate;
    }

    public void setHandoverDate(ZonedDateTimeFilter handoverDate) {
        this.handoverDate = handoverDate;
    }

    public StringFilter getHandoverBy() {
        return handoverBy;
    }

    public Optional<StringFilter> optionalHandoverBy() {
        return Optional.ofNullable(handoverBy);
    }

    public StringFilter handoverBy() {
        if (handoverBy == null) {
            setHandoverBy(new StringFilter());
        }
        return handoverBy;
    }

    public void setHandoverBy(StringFilter handoverBy) {
        this.handoverBy = handoverBy;
    }

    public UUIDFilter getUserId() {
        return userId;
    }

    public Optional<UUIDFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public UUIDFilter userId() {
        if (userId == null) {
            setUserId(new UUIDFilter());
        }
        return userId;
    }

    public void setUserId(UUIDFilter userId) {
        this.userId = userId;
    }

    public StringFilter getUserPosition() {
        return userPosition;
    }

    public Optional<StringFilter> optionalUserPosition() {
        return Optional.ofNullable(userPosition);
    }

    public StringFilter userPosition() {
        if (userPosition == null) {
            setUserPosition(new StringFilter());
        }
        return userPosition;
    }

    public void setUserPosition(StringFilter userPosition) {
        this.userPosition = userPosition;
    }

    public StringFilter getSeriesNumber() {
        return seriesNumber;
    }

    public Optional<StringFilter> optionalSeriesNumber() {
        return Optional.ofNullable(seriesNumber);
    }

    public StringFilter seriesNumber() {
        if (seriesNumber == null) {
            setSeriesNumber(new StringFilter());
        }
        return seriesNumber;
    }

    public void setSeriesNumber(StringFilter seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public ZonedDateTimeFilter getUsageDate() {
        return usageDate;
    }

    public Optional<ZonedDateTimeFilter> optionalUsageDate() {
        return Optional.ofNullable(usageDate);
    }

    public ZonedDateTimeFilter usageDate() {
        if (usageDate == null) {
            setUsageDate(new ZonedDateTimeFilter());
        }
        return usageDate;
    }

    public void setUsageDate(ZonedDateTimeFilter usageDate) {
        this.usageDate = usageDate;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public Optional<StringFilter> optionalInvoiceNumber() {
        return Optional.ofNullable(invoiceNumber);
    }

    public StringFilter invoiceNumber() {
        if (invoiceNumber == null) {
            setInvoiceNumber(new StringFilter());
        }
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public ZonedDateTimeFilter getInvoiceDate() {
        return invoiceDate;
    }

    public Optional<ZonedDateTimeFilter> optionalInvoiceDate() {
        return Optional.ofNullable(invoiceDate);
    }

    public ZonedDateTimeFilter invoiceDate() {
        if (invoiceDate == null) {
            setInvoiceDate(new ZonedDateTimeFilter());
        }
        return invoiceDate;
    }

    public void setInvoiceDate(ZonedDateTimeFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getLiquidationDate() {
        return liquidationDate;
    }

    public Optional<ZonedDateTimeFilter> optionalLiquidationDate() {
        return Optional.ofNullable(liquidationDate);
    }

    public ZonedDateTimeFilter liquidationDate() {
        if (liquidationDate == null) {
            setLiquidationDate(new ZonedDateTimeFilter());
        }
        return liquidationDate;
    }

    public void setLiquidationDate(ZonedDateTimeFilter liquidationDate) {
        this.liquidationDate = liquidationDate;
    }

    public UUIDFilter getUnit() {
        return unit;
    }

    public Optional<UUIDFilter> optionalUnit() {
        return Optional.ofNullable(unit);
    }

    public UUIDFilter unit() {
        if (unit == null) {
            setUnit(new UUIDFilter());
        }
        return unit;
    }

    public void setUnit(UUIDFilter unit) {
        this.unit = unit;
    }

    public IntegerFilter getYearOfUse() {
        return yearOfUse;
    }

    public Optional<IntegerFilter> optionalYearOfUse() {
        return Optional.ofNullable(yearOfUse);
    }

    public IntegerFilter yearOfUse() {
        if (yearOfUse == null) {
            setYearOfUse(new IntegerFilter());
        }
        return yearOfUse;
    }

    public void setYearOfUse(IntegerFilter yearOfUse) {
        this.yearOfUse = yearOfUse;
    }

    public IntegerFilter getMonthOfUse() {
        return monthOfUse;
    }

    public Optional<IntegerFilter> optionalMonthOfUse() {
        return Optional.ofNullable(monthOfUse);
    }

    public IntegerFilter monthOfUse() {
        if (monthOfUse == null) {
            setMonthOfUse(new IntegerFilter());
        }
        return monthOfUse;
    }

    public void setMonthOfUse(IntegerFilter monthOfUse) {
        this.monthOfUse = monthOfUse;
    }

    public FloatFilter getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public Optional<FloatFilter> optionalWarrantyPeriod() {
        return Optional.ofNullable(warrantyPeriod);
    }

    public FloatFilter warrantyPeriod() {
        if (warrantyPeriod == null) {
            setWarrantyPeriod(new FloatFilter());
        }
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(FloatFilter warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public Optional<StringFilter> optionalManufacturer() {
        return Optional.ofNullable(manufacturer);
    }

    public StringFilter manufacturer() {
        if (manufacturer == null) {
            setManufacturer(new StringFilter());
        }
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BooleanFilter getIsMadeIn() {
        return isMadeIn;
    }

    public Optional<BooleanFilter> optionalIsMadeIn() {
        return Optional.ofNullable(isMadeIn);
    }

    public BooleanFilter isMadeIn() {
        if (isMadeIn == null) {
            setIsMadeIn(new BooleanFilter());
        }
        return isMadeIn;
    }

    public void setIsMadeIn(BooleanFilter isMadeIn) {
        this.isMadeIn = isMadeIn;
    }

    public StringFilter getSpecs() {
        return specs;
    }

    public Optional<StringFilter> optionalSpecs() {
        return Optional.ofNullable(specs);
    }

    public StringFilter specs() {
        if (specs == null) {
            setSpecs(new StringFilter());
        }
        return specs;
    }

    public void setSpecs(StringFilter specs) {
        this.specs = specs;
    }

    public ZonedDateTimeFilter getRemovalDate() {
        return removalDate;
    }

    public Optional<ZonedDateTimeFilter> optionalRemovalDate() {
        return Optional.ofNullable(removalDate);
    }

    public ZonedDateTimeFilter removalDate() {
        if (removalDate == null) {
            setRemovalDate(new ZonedDateTimeFilter());
        }
        return removalDate;
    }

    public void setRemovalDate(ZonedDateTimeFilter removalDate) {
        this.removalDate = removalDate;
    }

    public StringFilter getReasonForRemoval() {
        return reasonForRemoval;
    }

    public Optional<StringFilter> optionalReasonForRemoval() {
        return Optional.ofNullable(reasonForRemoval);
    }

    public StringFilter reasonForRemoval() {
        if (reasonForRemoval == null) {
            setReasonForRemoval(new StringFilter());
        }
        return reasonForRemoval;
    }

    public void setReasonForRemoval(StringFilter reasonForRemoval) {
        this.reasonForRemoval = reasonForRemoval;
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
        final ItemInfoCriteria that = (ItemInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(handoverNumber, that.handoverNumber) &&
            Objects.equals(handoverDate, that.handoverDate) &&
            Objects.equals(handoverBy, that.handoverBy) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userPosition, that.userPosition) &&
            Objects.equals(seriesNumber, that.seriesNumber) &&
            Objects.equals(usageDate, that.usageDate) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(liquidationDate, that.liquidationDate) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(yearOfUse, that.yearOfUse) &&
            Objects.equals(monthOfUse, that.monthOfUse) &&
            Objects.equals(warrantyPeriod, that.warrantyPeriod) &&
            Objects.equals(manufacturer, that.manufacturer) &&
            Objects.equals(isMadeIn, that.isMadeIn) &&
            Objects.equals(specs, that.specs) &&
            Objects.equals(removalDate, that.removalDate) &&
            Objects.equals(reasonForRemoval, that.reasonForRemoval) &&
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
            name,
            registrationNumber,
            registrationDate,
            handoverNumber,
            handoverDate,
            handoverBy,
            userId,
            userPosition,
            seriesNumber,
            usageDate,
            invoiceNumber,
            invoiceDate,
            status,
            liquidationDate,
            unit,
            yearOfUse,
            monthOfUse,
            warrantyPeriod,
            manufacturer,
            isMadeIn,
            specs,
            removalDate,
            reasonForRemoval,
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
        return "ItemInfoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalRegistrationNumber().map(f -> "registrationNumber=" + f + ", ").orElse("") +
            optionalRegistrationDate().map(f -> "registrationDate=" + f + ", ").orElse("") +
            optionalHandoverNumber().map(f -> "handoverNumber=" + f + ", ").orElse("") +
            optionalHandoverDate().map(f -> "handoverDate=" + f + ", ").orElse("") +
            optionalHandoverBy().map(f -> "handoverBy=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalUserPosition().map(f -> "userPosition=" + f + ", ").orElse("") +
            optionalSeriesNumber().map(f -> "seriesNumber=" + f + ", ").orElse("") +
            optionalUsageDate().map(f -> "usageDate=" + f + ", ").orElse("") +
            optionalInvoiceNumber().map(f -> "invoiceNumber=" + f + ", ").orElse("") +
            optionalInvoiceDate().map(f -> "invoiceDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalLiquidationDate().map(f -> "liquidationDate=" + f + ", ").orElse("") +
            optionalUnit().map(f -> "unit=" + f + ", ").orElse("") +
            optionalYearOfUse().map(f -> "yearOfUse=" + f + ", ").orElse("") +
            optionalMonthOfUse().map(f -> "monthOfUse=" + f + ", ").orElse("") +
            optionalWarrantyPeriod().map(f -> "warrantyPeriod=" + f + ", ").orElse("") +
            optionalManufacturer().map(f -> "manufacturer=" + f + ", ").orElse("") +
            optionalIsMadeIn().map(f -> "isMadeIn=" + f + ", ").orElse("") +
            optionalSpecs().map(f -> "specs=" + f + ", ").orElse("") +
            optionalRemovalDate().map(f -> "removalDate=" + f + ", ").orElse("") +
            optionalReasonForRemoval().map(f -> "reasonForRemoval=" + f + ", ").orElse("") +
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
