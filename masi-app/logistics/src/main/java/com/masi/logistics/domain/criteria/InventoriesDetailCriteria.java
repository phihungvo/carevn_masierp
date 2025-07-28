package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.InventoriesDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.InventoriesDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private UUIDFilter itemId;

    private UUIDFilter inventoriesId;

    private BigDecimalFilter quantity;

    private BigDecimalFilter price;

    private BigDecimalFilter totalPrice;

    private StringFilter codeUom;

    private UUIDFilter uomId;

    private StringFilter uomName;

    private FloatFilter beforeItemInventory;

    private FloatFilter afterItemInventory;

    private BigDecimalFilter costPrice;

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

    public InventoriesDetailCriteria() {}

    public InventoriesDetailCriteria(InventoriesDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.itemId = other.optionalItemId().map(UUIDFilter::copy).orElse(null);
        this.inventoriesId = other.optionalInventoriesId().map(UUIDFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(BigDecimalFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.codeUom = other.optionalCodeUom().map(StringFilter::copy).orElse(null);
        this.uomId = other.optionalUomId().map(UUIDFilter::copy).orElse(null);
        this.uomName = other.optionalUomName().map(StringFilter::copy).orElse(null);
        this.beforeItemInventory = other.optionalBeforeItemInventory().map(FloatFilter::copy).orElse(null);
        this.afterItemInventory = other.optionalAfterItemInventory().map(FloatFilter::copy).orElse(null);
        this.costPrice = other.optionalCostPrice().map(BigDecimalFilter::copy).orElse(null);
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
    public InventoriesDetailCriteria copy() {
        return new InventoriesDetailCriteria(this);
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

    public UUIDFilter getInventoriesId() {
        return inventoriesId;
    }

    public Optional<UUIDFilter> optionalInventoriesId() {
        return Optional.ofNullable(inventoriesId);
    }

    public UUIDFilter inventoriesId() {
        if (inventoriesId == null) {
            setInventoriesId(new UUIDFilter());
        }
        return inventoriesId;
    }

    public void setInventoriesId(UUIDFilter inventoriesId) {
        this.inventoriesId = inventoriesId;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public Optional<BigDecimalFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public BigDecimalFilter quantity() {
        if (quantity == null) {
            setQuantity(new BigDecimalFilter());
        }
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public StringFilter getCodeUom() {
        return codeUom;
    }

    public Optional<StringFilter> optionalCodeUom() {
        return Optional.ofNullable(codeUom);
    }

    public StringFilter codeUom() {
        if (codeUom == null) {
            setCodeUom(new StringFilter());
        }
        return codeUom;
    }

    public void setCodeUom(StringFilter codeUom) {
        this.codeUom = codeUom;
    }

    public UUIDFilter getUomId() {
        return uomId;
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

    public void setUomId(UUIDFilter uomId) {
        this.uomId = uomId;
    }

    public StringFilter getUomName() {
        return uomName;
    }

    public Optional<StringFilter> optionalUomName() {
        return Optional.ofNullable(uomName);
    }

    public StringFilter uomName() {
        if (uomName == null) {
            setUomName(new StringFilter());
        }
        return uomName;
    }

    public void setUomName(StringFilter uomName) {
        this.uomName = uomName;
    }

    public FloatFilter getBeforeItemInventory() {
        return beforeItemInventory;
    }

    public Optional<FloatFilter> optionalBeforeItemInventory() {
        return Optional.ofNullable(beforeItemInventory);
    }

    public FloatFilter beforeItemInventory() {
        if (beforeItemInventory == null) {
            setBeforeItemInventory(new FloatFilter());
        }
        return beforeItemInventory;
    }

    public void setBeforeItemInventory(FloatFilter beforeItemInventory) {
        this.beforeItemInventory = beforeItemInventory;
    }

    public FloatFilter getAfterItemInventory() {
        return afterItemInventory;
    }

    public Optional<FloatFilter> optionalAfterItemInventory() {
        return Optional.ofNullable(afterItemInventory);
    }

    public FloatFilter afterItemInventory() {
        if (afterItemInventory == null) {
            setAfterItemInventory(new FloatFilter());
        }
        return afterItemInventory;
    }

    public void setAfterItemInventory(FloatFilter afterItemInventory) {
        this.afterItemInventory = afterItemInventory;
    }

    public BigDecimalFilter getCostPrice() {
        return costPrice;
    }

    public Optional<BigDecimalFilter> optionalCostPrice() {
        return Optional.ofNullable(costPrice);
    }

    public BigDecimalFilter costPrice() {
        if (costPrice == null) {
            setCostPrice(new BigDecimalFilter());
        }
        return costPrice;
    }

    public void setCostPrice(BigDecimalFilter costPrice) {
        this.costPrice = costPrice;
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
        final InventoriesDetailCriteria that = (InventoriesDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(itemId, that.itemId) &&
            Objects.equals(inventoriesId, that.inventoriesId) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(codeUom, that.codeUom) &&
            Objects.equals(uomId, that.uomId) &&
            Objects.equals(uomName, that.uomName) &&
            Objects.equals(beforeItemInventory, that.beforeItemInventory) &&
            Objects.equals(afterItemInventory, that.afterItemInventory) &&
            Objects.equals(costPrice, that.costPrice) &&
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
            code,
            itemId,
            inventoriesId,
            quantity,
            price,
            totalPrice,
            codeUom,
            uomId,
            uomName,
            beforeItemInventory,
            afterItemInventory,
            costPrice,
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
        return "InventoriesDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalItemId().map(f -> "itemId=" + f + ", ").orElse("") +
            optionalInventoriesId().map(f -> "inventoriesId=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalCodeUom().map(f -> "codeUom=" + f + ", ").orElse("") +
            optionalUomId().map(f -> "uomId=" + f + ", ").orElse("") +
            optionalUomName().map(f -> "uomName=" + f + ", ").orElse("") +
            optionalBeforeItemInventory().map(f -> "beforeItemInventory=" + f + ", ").orElse("") +
            optionalAfterItemInventory().map(f -> "afterItemInventory=" + f + ", ").orElse("") +
            optionalCostPrice().map(f -> "costPrice=" + f + ", ").orElse("") +
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
