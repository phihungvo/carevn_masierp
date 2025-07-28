package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import com.masi.logistics.domain.enumeration.ItemTypeCategory;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.ItemCategory} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ItemCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCategoryCriteria implements Serializable, Criteria {

    public static class ItemTypeCategoryFilter extends Filter<ItemTypeCategory>{
        public ItemTypeCategoryFilter() {}
        public ItemTypeCategoryFilter(ItemTypeCategoryFilter filter) {
            super(filter);
        }

        @Override
        public ItemTypeCategoryFilter copy() {
            return new ItemTypeCategoryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter company;

    private StringFilter department;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private UUIDFilter warehouseTypeId;

    private Boolean distinct;

    private ItemTypeCategoryFilter itemTypeCategory;

    public ItemCategoryCriteria() {}

    public ItemCategoryCriteria(ItemCategoryCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.warehouseTypeId = other.optionalWarehouseTypeId().map(UUIDFilter::copy).orElse(null);
        this.itemTypeCategory = other.optionalItemTypeCategory().map(ItemTypeCategoryFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ItemCategoryCriteria copy() {
        return new ItemCategoryCriteria(this);
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

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
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

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
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

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
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

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
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

    public Optional<ZonedDateTimeFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public ZonedDateTimeFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new ZonedDateTimeFilter());
        }
        return deletedAt;
    }

    public Optional<UUIDFilter> optionalWarehouseTypeId() {
        return Optional.ofNullable(warehouseTypeId);
    }

    public UUIDFilter warehouseTypeId() {
        if (warehouseTypeId == null) {
            setWarehouseTypeId(new UUIDFilter());
        }
        return warehouseTypeId;
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

    public Optional<ItemTypeCategoryFilter> optionalItemTypeCategory() {
        return Optional.ofNullable(itemTypeCategory);
    }

    public ItemTypeCategoryFilter itemTypeCategory() {
        if (itemTypeCategory == null) {
            setItemTypeCategory(new ItemTypeCategoryFilter());
        }
        return itemTypeCategory;
    }

}
