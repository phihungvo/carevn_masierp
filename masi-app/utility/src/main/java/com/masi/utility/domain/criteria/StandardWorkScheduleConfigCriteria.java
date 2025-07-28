package com.masi.utility.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.utility.domain.StandardWorkScheduleConfig} entity. This class is used
 * in {@link com.masi.utility.web.rest.StandardWorkScheduleConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /standard-work-schedule-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StandardWorkScheduleConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private IntegerFilter dayOfWeek;

    private IntegerFilter numberOfShifts;

    private IntegerFilter workHours;

    private StringFilter department;

    private StringFilter company;

    private StringFilter departmentType;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private Boolean distinct;

    public StandardWorkScheduleConfigCriteria() {}

    public StandardWorkScheduleConfigCriteria(StandardWorkScheduleConfigCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.dayOfWeek = other.optionalDayOfWeek().map(IntegerFilter::copy).orElse(null);
        this.numberOfShifts = other.optionalNumberOfShifts().map(IntegerFilter::copy).orElse(null);
        this.workHours = other.optionalWorkHours().map(IntegerFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.departmentType = other.optionalDepartmentType().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StandardWorkScheduleConfigCriteria copy() {
        return new StandardWorkScheduleConfigCriteria(this);
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

    public IntegerFilter getDayOfWeek() {
        return dayOfWeek;
    }

    public Optional<IntegerFilter> optionalDayOfWeek() {
        return Optional.ofNullable(dayOfWeek);
    }

    public IntegerFilter dayOfWeek() {
        if (dayOfWeek == null) {
            setDayOfWeek(new IntegerFilter());
        }
        return dayOfWeek;
    }

    public void setDayOfWeek(IntegerFilter dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public IntegerFilter getNumberOfShifts() {
        return numberOfShifts;
    }

    public Optional<IntegerFilter> optionalNumberOfShifts() {
        return Optional.ofNullable(numberOfShifts);
    }

    public IntegerFilter numberOfShifts() {
        if (numberOfShifts == null) {
            setNumberOfShifts(new IntegerFilter());
        }
        return numberOfShifts;
    }

    public void setNumberOfShifts(IntegerFilter numberOfShifts) {
        this.numberOfShifts = numberOfShifts;
    }

    public IntegerFilter getWorkHours() {
        return workHours;
    }

    public Optional<IntegerFilter> optionalWorkHours() {
        return Optional.ofNullable(workHours);
    }

    public IntegerFilter workHours() {
        if (workHours == null) {
            setWorkHours(new IntegerFilter());
        }
        return workHours;
    }

    public void setWorkHours(IntegerFilter workHours) {
        this.workHours = workHours;
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

    public StringFilter getDepartmentType() {
        return departmentType;
    }

    public Optional<StringFilter> optionalDepartmentType() {
        return Optional.ofNullable(departmentType);
    }

    public StringFilter departmentType() {
        if (departmentType == null) {
            setDepartmentType(new StringFilter());
        }
        return departmentType;
    }

    public void setDepartmentType(StringFilter departmentType) {
        this.departmentType = departmentType;
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
        final StandardWorkScheduleConfigCriteria that = (StandardWorkScheduleConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(numberOfShifts, that.numberOfShifts) &&
            Objects.equals(workHours, that.workHours) &&
            Objects.equals(department, that.department) &&
            Objects.equals(company, that.company) &&
            Objects.equals(departmentType, that.departmentType) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            dayOfWeek,
            numberOfShifts,
            workHours,
            department,
            company,
            departmentType,
            updatedAt,
            updatedBy,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StandardWorkScheduleConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDayOfWeek().map(f -> "dayOfWeek=" + f + ", ").orElse("") +
            optionalNumberOfShifts().map(f -> "numberOfShifts=" + f + ", ").orElse("") +
            optionalWorkHours().map(f -> "workHours=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDepartmentType().map(f -> "departmentType=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUpdatedBy().map(f -> "updatedBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
