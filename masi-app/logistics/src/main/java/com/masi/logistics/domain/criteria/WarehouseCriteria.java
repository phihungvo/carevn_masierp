package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.Warehouse} entity. This class is used
 * in {@link com.masi.logistics.web.rest.WarehouseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /warehouses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter address;

    private BooleanFilter active;

    private ZonedDateTimeFilter createAt;

    private StringFilter createBy;

    private ZonedDateTimeFilter updateAt;

    private StringFilter updateBy;

    private ZonedDateTimeFilter deleteAt;

    private StringFilter deleteBy;

    private StringFilter company;

    private UUIDFilter warehouseTypeId;

    private StringFilter warehouseTypePage;

    private Boolean isNotGetWareHouseUniform;

    private Boolean distinct;

    public WarehouseCriteria() {}

    public WarehouseCriteria(WarehouseCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createBy = other.optionalCreateBy().map(StringFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updateBy = other.optionalUpdateBy().map(StringFilter::copy).orElse(null);
        this.deleteAt = other.optionalDeleteAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleteBy = other.optionalDeleteBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.warehouseTypeId = other.optionalWarehouseTypeId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WarehouseCriteria copy() {
        return new WarehouseCriteria(this);
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

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public Optional<ZonedDateTimeFilter> optionalCreateAt() {
        return Optional.ofNullable(createAt);
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            setCreateAt(new ZonedDateTimeFilter());
        }
        return createAt;
    }

    public Optional<StringFilter> optionalCreateBy() {
        return Optional.ofNullable(createBy);
    }

    public StringFilter createBy() {
        if (createBy == null) {
            setCreateBy(new StringFilter());
        }
        return createBy;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdateAt() {
        return Optional.ofNullable(updateAt);
    }

    public ZonedDateTimeFilter updateAt() {
        if (updateAt == null) {
            setUpdateAt(new ZonedDateTimeFilter());
        }
        return updateAt;
    }

    public Optional<StringFilter> optionalUpdateBy() {
        return Optional.ofNullable(updateBy);
    }

    public StringFilter updateBy() {
        if (updateBy == null) {
            setUpdateBy(new StringFilter());
        }
        return updateBy;
    }

    public Optional<ZonedDateTimeFilter> optionalDeleteAt() {
        return Optional.ofNullable(deleteAt);
    }

    public ZonedDateTimeFilter deleteAt() {
        if (deleteAt == null) {
            setDeleteAt(new ZonedDateTimeFilter());
        }
        return deleteAt;
    }

    public Optional<StringFilter> optionalDeleteBy() {
        return Optional.ofNullable(deleteBy);
    }

    public StringFilter deleteBy() {
        if (deleteBy == null) {
            setDeleteBy(new StringFilter());
        }
        return deleteBy;
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

}
