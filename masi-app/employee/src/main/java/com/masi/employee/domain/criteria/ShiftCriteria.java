package com.masi.employee.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.employee.domain.Shift} entity. This class is used
 * in {@link com.masi.employee.web.rest.ShiftResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shifts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShiftCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter idStandardWorkScheduleConfig;

    private StringFilter shiftName;

    private IntegerFilter durationHours;

    private IntegerFilter hourStartTime;

    private IntegerFilter minuteStartTime;

    private IntegerFilter secondStartTime;

    private IntegerFilter hourEndTime;

    private IntegerFilter minuteEndTime;

    private IntegerFilter secondEndTime;

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

    public ShiftCriteria() {}

    public ShiftCriteria(ShiftCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.idStandardWorkScheduleConfig = other.optionalIdStandardWorkScheduleConfig().map(UUIDFilter::copy).orElse(null);
        this.shiftName = other.optionalShiftName().map(StringFilter::copy).orElse(null);
        this.durationHours = other.optionalDurationHours().map(IntegerFilter::copy).orElse(null);
        this.hourStartTime = other.optionalHourStartTime().map(IntegerFilter::copy).orElse(null);
        this.minuteStartTime = other.optionalMinuteStartTime().map(IntegerFilter::copy).orElse(null);
        this.secondStartTime = other.optionalSecondStartTime().map(IntegerFilter::copy).orElse(null);
        this.hourEndTime = other.optionalHourEndTime().map(IntegerFilter::copy).orElse(null);
        this.minuteEndTime = other.optionalMinuteEndTime().map(IntegerFilter::copy).orElse(null);
        this.secondEndTime = other.optionalSecondEndTime().map(IntegerFilter::copy).orElse(null);
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
    public ShiftCriteria copy() {
        return new ShiftCriteria(this);
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

    public UUIDFilter getIdStandardWorkScheduleConfig() {
        return idStandardWorkScheduleConfig;
    }

    public Optional<UUIDFilter> optionalIdStandardWorkScheduleConfig() {
        return Optional.ofNullable(idStandardWorkScheduleConfig);
    }

    public UUIDFilter idStandardWorkScheduleConfig() {
        if (idStandardWorkScheduleConfig == null) {
            setIdStandardWorkScheduleConfig(new UUIDFilter());
        }
        return idStandardWorkScheduleConfig;
    }

    public void setIdStandardWorkScheduleConfig(UUIDFilter idStandardWorkScheduleConfig) {
        this.idStandardWorkScheduleConfig = idStandardWorkScheduleConfig;
    }

    public StringFilter getShiftName() {
        return shiftName;
    }

    public Optional<StringFilter> optionalShiftName() {
        return Optional.ofNullable(shiftName);
    }

    public StringFilter shiftName() {
        if (shiftName == null) {
            setShiftName(new StringFilter());
        }
        return shiftName;
    }

    public void setShiftName(StringFilter shiftName) {
        this.shiftName = shiftName;
    }

    public IntegerFilter getDurationHours() {
        return durationHours;
    }

    public Optional<IntegerFilter> optionalDurationHours() {
        return Optional.ofNullable(durationHours);
    }

    public IntegerFilter durationHours() {
        if (durationHours == null) {
            setDurationHours(new IntegerFilter());
        }
        return durationHours;
    }

    public void setDurationHours(IntegerFilter durationHours) {
        this.durationHours = durationHours;
    }

    public IntegerFilter getHourStartTime() {
        return hourStartTime;
    }

    public Optional<IntegerFilter> optionalHourStartTime() {
        return Optional.ofNullable(hourStartTime);
    }

    public IntegerFilter hourStartTime() {
        if (hourStartTime == null) {
            setHourStartTime(new IntegerFilter());
        }
        return hourStartTime;
    }

    public void setHourStartTime(IntegerFilter hourStartTime) {
        this.hourStartTime = hourStartTime;
    }

    public IntegerFilter getMinuteStartTime() {
        return minuteStartTime;
    }

    public Optional<IntegerFilter> optionalMinuteStartTime() {
        return Optional.ofNullable(minuteStartTime);
    }

    public IntegerFilter minuteStartTime() {
        if (minuteStartTime == null) {
            setMinuteStartTime(new IntegerFilter());
        }
        return minuteStartTime;
    }

    public void setMinuteStartTime(IntegerFilter minuteStartTime) {
        this.minuteStartTime = minuteStartTime;
    }

    public IntegerFilter getSecondStartTime() {
        return secondStartTime;
    }

    public Optional<IntegerFilter> optionalSecondStartTime() {
        return Optional.ofNullable(secondStartTime);
    }

    public IntegerFilter secondStartTime() {
        if (secondStartTime == null) {
            setSecondStartTime(new IntegerFilter());
        }
        return secondStartTime;
    }

    public void setSecondStartTime(IntegerFilter secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public IntegerFilter getHourEndTime() {
        return hourEndTime;
    }

    public Optional<IntegerFilter> optionalHourEndTime() {
        return Optional.ofNullable(hourEndTime);
    }

    public IntegerFilter hourEndTime() {
        if (hourEndTime == null) {
            setHourEndTime(new IntegerFilter());
        }
        return hourEndTime;
    }

    public void setHourEndTime(IntegerFilter hourEndTime) {
        this.hourEndTime = hourEndTime;
    }

    public IntegerFilter getMinuteEndTime() {
        return minuteEndTime;
    }

    public Optional<IntegerFilter> optionalMinuteEndTime() {
        return Optional.ofNullable(minuteEndTime);
    }

    public IntegerFilter minuteEndTime() {
        if (minuteEndTime == null) {
            setMinuteEndTime(new IntegerFilter());
        }
        return minuteEndTime;
    }

    public void setMinuteEndTime(IntegerFilter minuteEndTime) {
        this.minuteEndTime = minuteEndTime;
    }

    public IntegerFilter getSecondEndTime() {
        return secondEndTime;
    }

    public Optional<IntegerFilter> optionalSecondEndTime() {
        return Optional.ofNullable(secondEndTime);
    }

    public IntegerFilter secondEndTime() {
        if (secondEndTime == null) {
            setSecondEndTime(new IntegerFilter());
        }
        return secondEndTime;
    }

    public void setSecondEndTime(IntegerFilter secondEndTime) {
        this.secondEndTime = secondEndTime;
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
        final ShiftCriteria that = (ShiftCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idStandardWorkScheduleConfig, that.idStandardWorkScheduleConfig) &&
            Objects.equals(shiftName, that.shiftName) &&
            Objects.equals(durationHours, that.durationHours) &&
            Objects.equals(hourStartTime, that.hourStartTime) &&
            Objects.equals(minuteStartTime, that.minuteStartTime) &&
            Objects.equals(secondStartTime, that.secondStartTime) &&
            Objects.equals(hourEndTime, that.hourEndTime) &&
            Objects.equals(minuteEndTime, that.minuteEndTime) &&
            Objects.equals(secondEndTime, that.secondEndTime) &&
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
            idStandardWorkScheduleConfig,
            shiftName,
            durationHours,
            hourStartTime,
            minuteStartTime,
            secondStartTime,
            hourEndTime,
            minuteEndTime,
            secondEndTime,
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
        return "ShiftCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdStandardWorkScheduleConfig().map(f -> "idStandardWorkScheduleConfig=" + f + ", ").orElse("") +
            optionalShiftName().map(f -> "shiftName=" + f + ", ").orElse("") +
            optionalDurationHours().map(f -> "durationHours=" + f + ", ").orElse("") +
            optionalHourStartTime().map(f -> "hourStartTime=" + f + ", ").orElse("") +
            optionalMinuteStartTime().map(f -> "minuteStartTime=" + f + ", ").orElse("") +
            optionalSecondStartTime().map(f -> "secondStartTime=" + f + ", ").orElse("") +
            optionalHourEndTime().map(f -> "hourEndTime=" + f + ", ").orElse("") +
            optionalMinuteEndTime().map(f -> "minuteEndTime=" + f + ", ").orElse("") +
            optionalSecondEndTime().map(f -> "secondEndTime=" + f + ", ").orElse("") +
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
