package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.InventoryCheck} entity. This class is used
 * in {@link com.masi.logistics.web.rest.InventoryCheckResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventory-checks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryCheckCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private UUIDFilter employeeId;

    private ZonedDateTimeFilter checkDate;

    private StringFilter notes;

    private BooleanFilter result;

    private ZonedDateTimeFilter createAt;

    private StringFilter createBy;

    private ZonedDateTimeFilter updateAt;

    private StringFilter updateBy;

    private ZonedDateTimeFilter deleteAt;

    private StringFilter deleteBy;

    private StringFilter company;

    private UUIDFilter inventoryTransactionsId;

    private Boolean distinct;

    public InventoryCheckCriteria() {}

    public InventoryCheckCriteria(InventoryCheckCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(UUIDFilter::copy).orElse(null);
        this.checkDate = other.optionalCheckDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.result = other.optionalResult().map(BooleanFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createBy = other.optionalCreateBy().map(StringFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updateBy = other.optionalUpdateBy().map(StringFilter::copy).orElse(null);
        this.deleteAt = other.optionalDeleteAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleteBy = other.optionalDeleteBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.inventoryTransactionsId = other.optionalInventoryTransactionsId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InventoryCheckCriteria copy() {
        return new InventoryCheckCriteria(this);
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

    public UUIDFilter getEmployeeId() {
        return employeeId;
    }

    public Optional<UUIDFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public UUIDFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new UUIDFilter());
        }
        return employeeId;
    }

    public void setEmployeeId(UUIDFilter employeeId) {
        this.employeeId = employeeId;
    }

    public ZonedDateTimeFilter getCheckDate() {
        return checkDate;
    }

    public Optional<ZonedDateTimeFilter> optionalCheckDate() {
        return Optional.ofNullable(checkDate);
    }

    public ZonedDateTimeFilter checkDate() {
        if (checkDate == null) {
            setCheckDate(new ZonedDateTimeFilter());
        }
        return checkDate;
    }

    public void setCheckDate(ZonedDateTimeFilter checkDate) {
        this.checkDate = checkDate;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public BooleanFilter getResult() {
        return result;
    }

    public Optional<BooleanFilter> optionalResult() {
        return Optional.ofNullable(result);
    }

    public BooleanFilter result() {
        if (result == null) {
            setResult(new BooleanFilter());
        }
        return result;
    }

    public void setResult(BooleanFilter result) {
        this.result = result;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
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

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public StringFilter getCreateBy() {
        return createBy;
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

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTimeFilter getUpdateAt() {
        return updateAt;
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

    public void setUpdateAt(ZonedDateTimeFilter updateAt) {
        this.updateAt = updateAt;
    }

    public StringFilter getUpdateBy() {
        return updateBy;
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

    public void setUpdateBy(StringFilter updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTimeFilter getDeleteAt() {
        return deleteAt;
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

    public void setDeleteAt(ZonedDateTimeFilter deleteAt) {
        this.deleteAt = deleteAt;
    }

    public StringFilter getDeleteBy() {
        return deleteBy;
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

    public void setDeleteBy(StringFilter deleteBy) {
        this.deleteBy = deleteBy;
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

    public UUIDFilter getInventoryTransactionsId() {
        return inventoryTransactionsId;
    }

    public Optional<UUIDFilter> optionalInventoryTransactionsId() {
        return Optional.ofNullable(inventoryTransactionsId);
    }

    public UUIDFilter inventoryTransactionsId() {
        if (inventoryTransactionsId == null) {
            setInventoryTransactionsId(new UUIDFilter());
        }
        return inventoryTransactionsId;
    }

    public void setInventoryTransactionsId(UUIDFilter inventoryTransactionsId) {
        this.inventoryTransactionsId = inventoryTransactionsId;
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
        final InventoryCheckCriteria that = (InventoryCheckCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(checkDate, that.checkDate) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(result, that.result) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(updateBy, that.updateBy) &&
            Objects.equals(deleteAt, that.deleteAt) &&
            Objects.equals(deleteBy, that.deleteBy) &&
            Objects.equals(company, that.company) &&
            Objects.equals(inventoryTransactionsId, that.inventoryTransactionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            employeeId,
            checkDate,
            notes,
            result,
            createAt,
            createBy,
            updateAt,
            updateBy,
            deleteAt,
            deleteBy,
            company,
            inventoryTransactionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryCheckCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalCheckDate().map(f -> "checkDate=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalResult().map(f -> "result=" + f + ", ").orElse("") +
            optionalCreateAt().map(f -> "createAt=" + f + ", ").orElse("") +
            optionalCreateBy().map(f -> "createBy=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalUpdateBy().map(f -> "updateBy=" + f + ", ").orElse("") +
            optionalDeleteAt().map(f -> "deleteAt=" + f + ", ").orElse("") +
            optionalDeleteBy().map(f -> "deleteBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalInventoryTransactionsId().map(f -> "inventoryTransactionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
