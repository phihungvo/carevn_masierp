package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import com.masi.logistics.domain.enumeration.ItemType;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.Item} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private UUIDFilter uomId;

    private StringFilter attribute;

    private StringFilter company;

    private StringFilter department;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter updatedBy;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter deletedBy;

    private ZonedDateTimeFilter deletedAt;

    private UUIDFilter itemCategoryId;

    private Boolean distinct;

    private BooleanFilter isActive;

    private StringFilter itemType;

    private String search;

    private Boolean status;

    private Boolean isFindPerProtein;

    public ItemCriteria() {}

    public ItemCriteria(ItemCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(UUIDFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedBy = other.optionalUpdatedBy().map(StringFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deletedBy = other.optionalDeletedBy().map(StringFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.itemCategoryId = other.optionalItemCategoryId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;

        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.itemType = other.optionalIdItemType().map(StringFilter::copy).orElse(null);
        this.search = other.optionalSearch().orElse(null);
        this.status = other.optionalStatus().orElse(null);

    }

    @Override
    public ItemCriteria copy() {
        return new ItemCriteria(this);
    }

    public Optional<StringFilter> optionalIdItemType() {return Optional.ofNullable(itemType);}

    public StringFilter itemType() {
        if (itemType == null) {
            setCode(new StringFilter());
        }
        return itemType;
    }

    public Optional<BooleanFilter> optionalIsActive() {return Optional.ofNullable(isActive);}

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
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

    public Optional<UUIDFilter> optionalUomId() {
        return Optional.ofNullable(uomId);
    }

    public UUIDFilter uomId() {
        if (uomId == null) {
            setUomId(new UUIDFilter());
        }
        return uomId;
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

    public Optional<UUIDFilter> optionalItemCategoryId() {
        return Optional.ofNullable(itemCategoryId);
    }

    public UUIDFilter itemCategoryId() {
        if (itemCategoryId == null) {
            setItemCategoryId(new UUIDFilter());
        }
        return itemCategoryId;
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

    private Optional<String> optionalSearch() {
        return Optional.ofNullable(search);
    }

    public String search() {
        if (search == null) {
            setSearch(null);
        }
        return search;
    }

    private Optional<Boolean> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public Boolean status() {
        if (status == null) {
            setStatus(null);
        }
        return status;
    }

}
