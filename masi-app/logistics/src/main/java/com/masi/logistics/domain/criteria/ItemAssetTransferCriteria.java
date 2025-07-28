package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.ItemAssetTransfer} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ItemAssetTransferResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-asset-transfers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetTransferCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter attribute;

    private StringFilter name;

    private StringFilter status;

    private UUIDFilter inventoriesStorageId;

    private UUIDFilter transactionTypeId;

    private UUIDFilter itemCategoryId;

    private ZonedDateTimeFilter transferDate;

    private StringFilter fromUnit;

    private UUIDFilter fromDepartmentId;

    private UUIDFilter toDepartmentId;

    private UUIDFilter fromPersonId;

    private UUIDFilter toPersonId;

    private StringFilter fromAddress;

    private StringFilter toAddress;

    private BooleanFilter isDeleted;

    private ZonedDateTimeFilter createdAt;

    private StringFilter createdBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter deletedAt;

    private StringFilter deletedBy;

    private StringFilter company;

    private StringFilter department;

    private String search;

    private Boolean distinct;

    public ItemAssetTransferCriteria() {}

    public ItemAssetTransferCriteria(ItemAssetTransferCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.inventoriesStorageId = other.optionalInventoriesStorageId().map(UUIDFilter::copy).orElse(null);
        this.transactionTypeId = other.optionalTransactionTypeId().map(UUIDFilter::copy).orElse(null);
        this.itemCategoryId = other.optionalItemCategoryId().map(UUIDFilter::copy).orElse(null);
        this.transferDate = other.optionalTransferDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.fromUnit = other.optionalFromUnit().map(StringFilter::copy).orElse(null);
        this.fromDepartmentId = other.optionalFromDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.toDepartmentId = other.optionalToDepartmentId().map(UUIDFilter::copy).orElse(null);
        this.fromPersonId = other.optionalFromPersonId().map(UUIDFilter::copy).orElse(null);
        this.toPersonId = other.optionalToPersonId().map(UUIDFilter::copy).orElse(null);
        this.fromAddress = other.optionalFromAddress().map(StringFilter::copy).orElse(null);
        this.toAddress = other.optionalToAddress().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ItemAssetTransferCriteria copy() {
        return new ItemAssetTransferCriteria(this);
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

    public Optional<StringFilter> optionalAttribute() {
        return Optional.ofNullable(attribute);
    }

    public StringFilter attribute() {
        if (attribute == null) {
            setAttribute(new StringFilter());
        }
        return attribute;
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

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public Optional<UUIDFilter> optionalInventoriesStorageId() {
        return Optional.ofNullable(inventoriesStorageId);
    }

    public UUIDFilter inventoriesStorageId() {
        if (inventoriesStorageId == null) {
            setInventoriesStorageId(new UUIDFilter());
        }
        return inventoriesStorageId;
    }

    public Optional<UUIDFilter> optionalTransactionTypeId() {
        return Optional.ofNullable(transactionTypeId);
    }

    public UUIDFilter transactionTypeId() {
        if (transactionTypeId == null) {
            setTransactionTypeId(new UUIDFilter());
        }
        return transactionTypeId;
    }

    public Optional<UUIDFilter> optionalItemCategoryId() {
        return Optional.ofNullable(itemCategoryId);
    }

    public UUIDFilter itemCategoryId() {
        if (itemCategoryId == null) {
            setItemCategoryId(new UUIDFilter());
        }
        return itemCategoryId;
    }

    public Optional<ZonedDateTimeFilter> optionalTransferDate() {
        return Optional.ofNullable(transferDate);
    }

    public ZonedDateTimeFilter transferDate() {
        if (transferDate == null) {
            setTransferDate(new ZonedDateTimeFilter());
        }
        return transferDate;
    }

    public Optional<StringFilter> optionalFromUnit() {
        return Optional.ofNullable(fromUnit);
    }

    public StringFilter fromUnit() {
        if (fromUnit == null) {
            setFromUnit(new StringFilter());
        }
        return fromUnit;
    }

    public Optional<UUIDFilter> optionalFromDepartmentId() {
        return Optional.ofNullable(fromDepartmentId);
    }

    public UUIDFilter fromDepartmentId() {
        if (fromDepartmentId == null) {
            setFromDepartmentId(new UUIDFilter());
        }
        return fromDepartmentId;
    }

    public Optional<UUIDFilter> optionalToDepartmentId() {
        return Optional.ofNullable(toDepartmentId);
    }

    public UUIDFilter toDepartmentId() {
        if (toDepartmentId == null) {
            setToDepartmentId(new UUIDFilter());
        }
        return toDepartmentId;
    }

    public Optional<UUIDFilter> optionalFromPersonId() {
        return Optional.ofNullable(fromPersonId);
    }

    public UUIDFilter fromPersonId() {
        if (fromPersonId == null) {
            setFromPersonId(new UUIDFilter());
        }
        return fromPersonId;
    }

    public Optional<UUIDFilter> optionalToPersonId() {
        return Optional.ofNullable(toPersonId);
    }

    public UUIDFilter toPersonId() {
        if (toPersonId == null) {
            setToPersonId(new UUIDFilter());
        }
        return toPersonId;
    }

    public Optional<StringFilter> optionalFromAddress() {
        return Optional.ofNullable(fromAddress);
    }

    public StringFilter fromAddress() {
        if (fromAddress == null) {
            setFromAddress(new StringFilter());
        }
        return fromAddress;
    }

    public Optional<StringFilter> optionalToAddress() {
        return Optional.ofNullable(toAddress);
    }

    public StringFilter toAddress() {
        if (toAddress == null) {
            setToAddress(new StringFilter());
        }
        return toAddress;
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

    public Optional<String> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public String search() {
        if (search == null) {
            setSearch(null);
        }
        return search;
    }

}
