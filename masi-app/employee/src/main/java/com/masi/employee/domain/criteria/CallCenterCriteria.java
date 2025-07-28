package com.masi.employee.domain.criteria;

import com.masi.employee.domain.enumeration.GroupCs;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.domain.enumeration.TypeCS;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.employee.domain.CallCenter} entity. This class is used
 * in {@link com.masi.employee.web.rest.CallCenterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /call-centers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CallCenterCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GroupCs
     */
    public static class GroupCsFilter extends Filter<GroupCs> {

        public GroupCsFilter() {}

        public GroupCsFilter(GroupCsFilter filter) {
            super(filter);
        }

        @Override
        public GroupCsFilter copy() {
            return new GroupCsFilter(this);
        }
    }

    /**
     * Class for filtering StatusEntity
     */
    public static class StatusEntityFilter extends Filter<StatusEntity> {

        public StatusEntityFilter() {}

        public StatusEntityFilter(StatusEntityFilter filter) {
            super(filter);
        }

        @Override
        public StatusEntityFilter copy() {
            return new StatusEntityFilter(this);
        }
    }

    /**
     * Class for filtering TypeCS
     */
    public static class TypeCSFilter extends Filter<TypeCS> {

        public TypeCSFilter() {}

        public TypeCSFilter(TypeCSFilter filter) {
            super(filter);
        }

        @Override
        public TypeCSFilter copy() {
            return new TypeCSFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private ZonedDateTimeFilter receptionDate;

    private GroupCsFilter groupCS;

    private StringFilter phoneOfCaller;

    private StringFilter phoneOfName;

    private StatusEntityFilter status;

    private TypeCSFilter typeCS;

    private UUIDFilter customerId;

    private UUIDFilter employeeCreatedId;

    private StringFilter attribute;

    private StringFilter typePageCs;

    private UUIDFilter employeeAssignId;

    private UUIDFilter employeeCloseId;

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

    public CallCenterCriteria() {}

    public CallCenterCriteria(CallCenterCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.receptionDate = other.optionalReceptionDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.groupCS = other.optionalGroupCS().map(GroupCsFilter::copy).orElse(null);
        this.phoneOfCaller = other.optionalPhoneOfCaller().map(StringFilter::copy).orElse(null);
        this.phoneOfName = other.optionalPhoneOfName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StatusEntityFilter::copy).orElse(null);
        this.typeCS = other.optionalTypeCS().map(TypeCSFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(UUIDFilter::copy).orElse(null);
        this.employeeCreatedId = other.optionalEmployeeCreatedId().map(UUIDFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.employeeAssignId = other.optionalEmployeeAssignId().map(UUIDFilter::copy).orElse(null);
        this.employeeCloseId = other.optionalEmployeeCloseId().map(UUIDFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.typePageCs = other.optionalTypePageCs().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CallCenterCriteria copy() {
        return new CallCenterCriteria(this);
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

    public Optional<StringFilter> optionalTypePageCs() {
        return Optional.ofNullable(typePageCs);
    }

    public StringFilter typePageCs() {
        if (typePageCs == null) {
            setTypePageCs(new StringFilter());
        }
        return typePageCs;
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

    public Optional<ZonedDateTimeFilter> optionalReceptionDate() {
        return Optional.ofNullable(receptionDate);
    }

    public ZonedDateTimeFilter receptionDate() {
        if (receptionDate == null) {
            setReceptionDate(new ZonedDateTimeFilter());
        }
        return receptionDate;
    }

    public Optional<GroupCsFilter> optionalGroupCS() {
        return Optional.ofNullable(groupCS);
    }

    public GroupCsFilter groupCS() {
        if (groupCS == null) {
            setGroupCS(new GroupCsFilter());
        }
        return groupCS;
    }

    public Optional<StringFilter> optionalPhoneOfCaller() {
        return Optional.ofNullable(phoneOfCaller);
    }

    public StringFilter phoneOfCaller() {
        if (phoneOfCaller == null) {
            setPhoneOfCaller(new StringFilter());
        }
        return phoneOfCaller;
    }

    public Optional<StringFilter> optionalPhoneOfName() {
        return Optional.ofNullable(phoneOfName);
    }

    public StringFilter phoneOfName() {
        if (phoneOfName == null) {
            setPhoneOfName(new StringFilter());
        }
        return phoneOfName;
    }

    public Optional<StatusEntityFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StatusEntityFilter status() {
        if (status == null) {
            setStatus(new StatusEntityFilter());
        }
        return status;
    }

    public Optional<TypeCSFilter> optionalTypeCS() {
        return Optional.ofNullable(typeCS);
    }

    public TypeCSFilter typeCS() {
        if (typeCS == null) {
            setTypeCS(new TypeCSFilter());
        }
        return typeCS;
    }

    public Optional<UUIDFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public UUIDFilter customerId() {
        if (customerId == null) {
            setCustomerId(new UUIDFilter());
        }
        return customerId;
    }

    public Optional<UUIDFilter> optionalEmployeeCreatedId() {
        return Optional.ofNullable(employeeCreatedId);
    }

    public UUIDFilter employeeCreatedId() {
        if (employeeCreatedId == null) {
            setEmployeeCreatedId(new UUIDFilter());
        }
        return employeeCreatedId;
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

    public Optional<UUIDFilter> optionalEmployeeAssignId() {
        return Optional.ofNullable(employeeAssignId);
    }

    public UUIDFilter employeeAssignId() {
        if (employeeAssignId == null) {
            setEmployeeAssignId(new UUIDFilter());
        }
        return employeeAssignId;
    }

    public Optional<UUIDFilter> optionalEmployeeCloseId() {
        return Optional.ofNullable(employeeCloseId);
    }

    public UUIDFilter employeeCloseId() {
        if (employeeCloseId == null) {
            setEmployeeCloseId(new UUIDFilter());
        }
        return employeeCloseId;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
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

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
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

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

}
