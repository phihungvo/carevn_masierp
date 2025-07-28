package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.InventoriesStorage} entity. This class is used
 * in {@link com.masi.logistics.web.rest.InventoriesStorageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories-storages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesStorageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private UUIDFilter itemId;

    private UUIDFilter inventoriesDetailId;

    private ZonedDateTimeFilter importDate;

    private ZonedDateTimeFilter exportDate;

    private StringFilter depreciation;

    private ZonedDateTimeFilter expiryDate;

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

    private UUIDFilter supplierId;

    private UUIDFilter uomId;

    private StringFilter status;

    private StringFilter statusDepreciation;

    private UUIDFilter itemCategoryId;

    private Boolean checkDepreciation;

    private Boolean checkFishMeal;

    private String statusDevice;

    private StringFilter userPosition;

    private Boolean distinct;

    private Boolean checkMaterial;

    public InventoriesStorageCriteria() {}

    public InventoriesStorageCriteria(InventoriesStorageCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(UUIDFilter::copy).orElse(null);
        this.inventoriesDetailId = other.optionalInventoriesDetailId().map(UUIDFilter::copy).orElse(null);
        this.importDate = other.optionalImportDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.exportDate = other.optionalExportDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.depreciation = other.optionalDepreciation().map(StringFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(ZonedDateTimeFilter::copy).orElse(null);
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
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(UUIDFilter::copy).orElse(null);
        this.itemCategoryId = other.optionalItemCategoryId().map(UUIDFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.statusDepreciation = other.optionalStatusDepreciation().map(StringFilter::copy).orElse(null);
        this.checkDepreciation = other.optionalCheckDepreciation().orElse(null);
        this.checkFishMeal = other.optionalCheckFishMeal().orElse(null);
        this.statusDevice = other.optionalStatusDevice().orElse(null);
        this.userPosition = other.optionalUserPosition().map(StringFilter::copy).orElse(null);

        this.distinct = other.distinct;
    }

    @Override
    public InventoriesStorageCriteria copy() {
        return new InventoriesStorageCriteria(this);
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

    public Optional<UUIDFilter> optionalItemCategoryId() {
        return Optional.ofNullable(itemCategoryId);
    }

    public UUIDFilter itemCategoryId() {
        if (itemCategoryId == null) {
            setItemCategoryId(new UUIDFilter());
        }
        return itemCategoryId;
    }

    public Optional<StringFilter> optionalStatusDepreciation() {
        return Optional.ofNullable(statusDepreciation);
    }

    public StringFilter statusDepreciation() {
        if (statusDepreciation == null) {
            setStatusDepreciation(new StringFilter());
        }
        return statusDepreciation;
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


    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
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

    public Optional<UUIDFilter> optionalInventoriesDetailId() {
        return Optional.ofNullable(inventoriesDetailId);
    }

    public UUIDFilter inventoriesDetailId() {
        if (inventoriesDetailId == null) {
            setInventoriesDetailId(new UUIDFilter());
        }
        return inventoriesDetailId;
    }

    public Optional<ZonedDateTimeFilter> optionalImportDate() {
        return Optional.ofNullable(importDate);
    }

    public ZonedDateTimeFilter importDate() {
        if (importDate == null) {
            setImportDate(new ZonedDateTimeFilter());
        }
        return importDate;
    }

    public Optional<ZonedDateTimeFilter> optionalExportDate() {
        return Optional.ofNullable(exportDate);
    }

    public ZonedDateTimeFilter exportDate() {
        if (exportDate == null) {
            setExportDate(new ZonedDateTimeFilter());
        }
        return exportDate;
    }

    public Optional<StringFilter> optionalDepreciation() {
        return Optional.ofNullable(depreciation);
    }

    public StringFilter depreciation() {
        if (depreciation == null) {
            setDepreciation(new StringFilter());
        }
        return depreciation;
    }

    public Optional<ZonedDateTimeFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public ZonedDateTimeFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new ZonedDateTimeFilter());
        }
        return expiryDate;
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

    public Optional<UUIDFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public UUIDFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new UUIDFilter());
        }
        return supplierId;
    }

    public Optional<UUIDFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public UUIDFilter uomId() {
        if (uomId == null) {
            setUomId(new UUIDFilter());
        }
        return uomId;
    }

    public Optional<String> optionalStatusDevice() {
        return Optional.ofNullable(statusDevice);
    }

    public String statusDevice() {
        if (statusDevice == null) {
            setStatusDevice("");
        }
        return statusDevice;
    }

    public Optional<Boolean> optionalCheckDepreciation() {
        return Optional.ofNullable(checkDepreciation);
    }

    public Boolean checkDepreciation() {
        if (checkDepreciation == null) {
            setCheckDepreciation(false);
        }
        return checkDepreciation;
    }

    public Optional<Boolean> optionalCheckFishMeal() {
        return Optional.ofNullable(checkFishMeal);
    }

    public Boolean checkFishMeal() {
        if (checkFishMeal == null) {
            setCheckFishMeal(false);
        }
        return checkFishMeal;
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

}
