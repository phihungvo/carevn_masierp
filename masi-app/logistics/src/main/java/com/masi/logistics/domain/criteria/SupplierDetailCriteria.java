package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.SupplierDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.SupplierDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter supplierId;

    private UUIDFilter itemId;

    private BigDecimalFilter basePrice;

    private StringFilter notes;

    private ZonedDateTimeFilter createAt;

    private StringFilter createBy;

    private ZonedDateTimeFilter updateAt;

    private StringFilter updateBy;

    private ZonedDateTimeFilter deleteAt;

    private StringFilter deleteBy;

    private StringFilter company;

    private Boolean distinct;

    public SupplierDetailCriteria() {}

    public SupplierDetailCriteria(SupplierDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(UUIDFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(UUIDFilter::copy).orElse(null);
        this.basePrice = other.optionalBasePrice().map(BigDecimalFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createBy = other.optionalCreateBy().map(StringFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updateBy = other.optionalUpdateBy().map(StringFilter::copy).orElse(null);
        this.deleteAt = other.optionalDeleteAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleteBy = other.optionalDeleteBy().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplierDetailCriteria copy() {
        return new SupplierDetailCriteria(this);
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

    public UUIDFilter getSupplierId() {
        return supplierId;
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

    public void setSupplierId(UUIDFilter supplierId) {
        this.supplierId = supplierId;
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

    public BigDecimalFilter getBasePrice() {
        return basePrice;
    }

    public Optional<BigDecimalFilter> optionalBasePrice() {
        return Optional.ofNullable(basePrice);
    }

    public BigDecimalFilter basePrice() {
        if (basePrice == null) {
            setBasePrice(new BigDecimalFilter());
        }
        return basePrice;
    }

    public void setBasePrice(BigDecimalFilter basePrice) {
        this.basePrice = basePrice;
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
        final SupplierDetailCriteria that = (SupplierDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(basePrice, that.basePrice) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(updateBy, that.updateBy) &&
            Objects.equals(deleteAt, that.deleteAt) &&
            Objects.equals(deleteBy, that.deleteBy) &&
            Objects.equals(company, that.company) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            supplierId,
            itemId,
            basePrice,
            notes,
            createAt,
            createBy,
            updateAt,
            updateBy,
            deleteAt,
            deleteBy,
            company,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalBasePrice().map(f -> "basePrice=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCreateAt().map(f -> "createAt=" + f + ", ").orElse("") +
            optionalCreateBy().map(f -> "createBy=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalUpdateBy().map(f -> "updateBy=" + f + ", ").orElse("") +
            optionalDeleteAt().map(f -> "deleteAt=" + f + ", ").orElse("") +
            optionalDeleteBy().map(f -> "deleteBy=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
