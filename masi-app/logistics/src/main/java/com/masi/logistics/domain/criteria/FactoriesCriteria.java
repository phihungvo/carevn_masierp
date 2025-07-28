package com.masi.logistics.domain.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.enumeration.TypeFactory;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.Factories} entity. This class is used
 * in {@link com.masi.logistics.web.rest.FactoriesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactoriesCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeFactory
     */
    public static class TypeFactoryFilter extends Filter<TypeFactory> {

        public TypeFactoryFilter() {}

        public TypeFactoryFilter(TypeFactoryFilter filter) {
            super(filter);
        }

        @Override
        public TypeFactoryFilter copy() {
            return new TypeFactoryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private TypeFactoryFilter typeFactory;

    private UUIDFilter departmentId;

    private StringFilter description;

    private BooleanFilter canDelete;

    private StringFilter normalizedName;

    private StringFilter address;

    private UUIDFilter employeeOwnerId;

    private StringFilter attribute;

    private BooleanFilter isActive;

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

    public FactoriesCriteria() {}

    public FactoriesCriteria(FactoriesCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.typeFactory = other.optionalTypeFactory().map(TypeFactoryFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.canDelete = other.optionalCanDelete().map(BooleanFilter::copy).orElse(null);
        this.normalizedName = other.optionalNormalizedName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.employeeOwnerId = other.optionalEmployeeOwnerId().map(UUIDFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
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
    public FactoriesCriteria copy() {
        return new FactoriesCriteria(this);
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

    public TypeFactoryFilter getTypeFactory() {
        return typeFactory;
    }

    public Optional<TypeFactoryFilter> optionalTypeFactory() {
        return Optional.ofNullable(typeFactory);
    }

    public TypeFactoryFilter typeFactory() {
        if (typeFactory == null) {
            setTypeFactory(new TypeFactoryFilter());
        }
        return typeFactory;
    }

    public void setTypeFactory(TypeFactoryFilter typeFactory) {
        this.typeFactory = typeFactory;
    }

    public UUIDFilter getDepartmentId() {
        return departmentId;
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

    public void setDepartmentId(UUIDFilter departmentId) {
        this.departmentId = departmentId;
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

    public BooleanFilter getCanDelete() {
        return canDelete;
    }

    public Optional<BooleanFilter> optionalCanDelete() {
        return Optional.ofNullable(canDelete);
    }

    public BooleanFilter canDelete() {
        if (canDelete == null) {
            setCanDelete(new BooleanFilter());
        }
        return canDelete;
    }

    public void setCanDelete(BooleanFilter canDelete) {
        this.canDelete = canDelete;
    }

    public StringFilter getNormalizedName() {
        return normalizedName;
    }

    public Optional<StringFilter> optionalNormalizedName() {
        return Optional.ofNullable(normalizedName);
    }

    public StringFilter normalizedName() {
        if (normalizedName == null) {
            setNormalizedName(new StringFilter());
        }
        return normalizedName;
    }

    public void setNormalizedName(StringFilter normalizedName) {
        this.normalizedName = normalizedName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public UUIDFilter getEmployeeOwnerId() {
        return employeeOwnerId;
    }

    public Optional<UUIDFilter> optionalEmployeeOwnerId() {
        return Optional.ofNullable(employeeOwnerId);
    }

    public UUIDFilter employeeOwnerId() {
        if (employeeOwnerId == null) {
            setEmployeeOwnerId(new UUIDFilter());
        }
        return employeeOwnerId;
    }

    public void setEmployeeOwnerId(UUIDFilter employeeOwnerId) {
        this.employeeOwnerId = employeeOwnerId;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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
        final FactoriesCriteria that = (FactoriesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(typeFactory, that.typeFactory) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(description, that.description) &&
            Objects.equals(canDelete, that.canDelete) &&
            Objects.equals(normalizedName, that.normalizedName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(employeeOwnerId, that.employeeOwnerId) &&
            Objects.equals(attribute, that.attribute) &&
            Objects.equals(isActive, that.isActive) &&
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
            code,
            name,
            typeFactory,
            departmentId,
            description,
            canDelete,
            normalizedName,
            address,
            employeeOwnerId,
            attribute,
            isActive,
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
        return "FactoriesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTypeFactory().map(f -> "typeFactory=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCanDelete().map(f -> "canDelete=" + f + ", ").orElse("") +
            optionalNormalizedName().map(f -> "normalizedName=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalEmployeeOwnerId().map(f -> "employeeOwnerId=" + f + ", ").orElse("") +
            optionalAttribute().map(f -> "attribute=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
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
