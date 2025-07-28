package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.InventoriesCheckDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.InventoriesCheckDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories-check-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesCheckDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter inventoriesCheckId;

    private StringFilter code;

    private UUIDFilter itemId;

    private BigDecimalFilter systemQuantity;

    private BigDecimalFilter actualQuantity;

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

    public InventoriesCheckDetailCriteria() {}

    public InventoriesCheckDetailCriteria(InventoriesCheckDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.inventoriesCheckId = other.optionalInventoriesCheckId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(UUIDFilter::copy).orElse(null);
        this.systemQuantity = other.optionalSystemQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.actualQuantity = other.optionalActualQuantity().map(BigDecimalFilter::copy).orElse(null);
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
    public InventoriesCheckDetailCriteria copy() {
        return new InventoriesCheckDetailCriteria(this);
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

    public UUIDFilter getInventoriesCheckId() {
        return inventoriesCheckId;
    }

    public Optional<UUIDFilter> optionalInventoriesCheckId() {
        return Optional.ofNullable(inventoriesCheckId);
    }

    public UUIDFilter inventoriesCheckId() {
        if (inventoriesCheckId == null) {
            setInventoriesCheckId(new UUIDFilter());
        }
        return inventoriesCheckId;
    }

    public void setInventoriesCheckId(UUIDFilter inventoriesCheckId) {
        this.inventoriesCheckId = inventoriesCheckId;
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

    public UUIDFilter getItemId() {
        return itemId;
    }

    public Optional<UUIDFilter> optionalItemId() {
        return Optional.ofNullable(itemId);
    }

    public UUIDFilter itemId() {
        if (itemId == null) {
            setItemId(new UUIDFilter());
        }
        return itemId;
    }

    public void setItemId(UUIDFilter itemId) {
        this.itemId = itemId;
    }

    public BigDecimalFilter getSystemQuantity() {
        return systemQuantity;
    }

    public Optional<BigDecimalFilter> optionalSystemQuantity() {
        return Optional.ofNullable(systemQuantity);
    }

    public BigDecimalFilter systemQuantity() {
        if (systemQuantity == null) {
            setSystemQuantity(new BigDecimalFilter());
        }
        return systemQuantity;
    }

    public void setSystemQuantity(BigDecimalFilter systemQuantity) {
        this.systemQuantity = systemQuantity;
    }

    public BigDecimalFilter getActualQuantity() {
        return actualQuantity;
    }

    public Optional<BigDecimalFilter> optionalActualQuantity() {
        return Optional.ofNullable(actualQuantity);
    }

    public BigDecimalFilter actualQuantity() {
        if (actualQuantity == null) {
            setActualQuantity(new BigDecimalFilter());
        }
        return actualQuantity;
    }

    public void setActualQuantity(BigDecimalFilter actualQuantity) {
        this.actualQuantity = actualQuantity;
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
        final InventoriesCheckDetailCriteria that = (InventoriesCheckDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(inventoriesCheckId, that.inventoriesCheckId) &&
            Objects.equals(code, that.code) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(systemQuantity, that.systemQuantity) &&
            Objects.equals(actualQuantity, that.actualQuantity) &&
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
            inventoriesCheckId,
            code,
            itemId,
            systemQuantity,
            actualQuantity,
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
        return "InventoriesCheckDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInventoriesCheckId().map(f -> "inventoriesCheckId=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalSystemQuantity().map(f -> "systemQuantity=" + f + ", ").orElse("") +
            optionalActualQuantity().map(f -> "actualQuantity=" + f + ", ").orElse("") +
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
