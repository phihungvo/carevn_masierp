package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.ItemAssetDepreciationDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.ItemAssetDepreciationDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /item-asset-depreciation-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetDepreciationDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter attribute;

    private UUIDFilter inventoriesStorageId;

    private StringFilter costInformation;

    private StringFilter amortizedCostInformation;

    private BigDecimalFilter amortizationAmount;

    private BigDecimalFilter amortizationRate;

    private BigDecimalFilter accumulatedAmortizationAmount;

    private StringFilter recipe;

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

    public ItemAssetDepreciationDetailCriteria() {}

    public ItemAssetDepreciationDetailCriteria(ItemAssetDepreciationDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.attribute = other.optionalAttribute().map(StringFilter::copy).orElse(null);
        this.inventoriesStorageId = other.optionalInventoriesStorageId().map(UUIDFilter::copy).orElse(null);
        this.costInformation = other.optionalCostInformation().map(StringFilter::copy).orElse(null);
        this.amortizedCostInformation = other.optionalAmortizedCostInformation().map(StringFilter::copy).orElse(null);
        this.amortizationAmount = other.optionalAmortizationAmount().map(BigDecimalFilter::copy).orElse(null);
        this.amortizationRate = other.optionalAmortizationRate().map(BigDecimalFilter::copy).orElse(null);
        this.accumulatedAmortizationAmount = other.optionalAccumulatedAmortizationAmount().map(BigDecimalFilter::copy).orElse(null);
        this.recipe = other.optionalRecipe().map(StringFilter::copy).orElse(null);
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
    public ItemAssetDepreciationDetailCriteria copy() {
        return new ItemAssetDepreciationDetailCriteria(this);
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

    public UUIDFilter getInventoriesStorageId() {
        return inventoriesStorageId;
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

    public void setInventoriesStorageId(UUIDFilter inventoriesStorageId) {
        this.inventoriesStorageId = inventoriesStorageId;
    }

    public StringFilter getCostInformation() {
        return costInformation;
    }

    public Optional<StringFilter> optionalCostInformation() {
        return Optional.ofNullable(costInformation);
    }

    public StringFilter costInformation() {
        if (costInformation == null) {
            setCostInformation(new StringFilter());
        }
        return costInformation;
    }

    public void setCostInformation(StringFilter costInformation) {
        this.costInformation = costInformation;
    }

    public StringFilter getAmortizedCostInformation() {
        return amortizedCostInformation;
    }

    public Optional<StringFilter> optionalAmortizedCostInformation() {
        return Optional.ofNullable(amortizedCostInformation);
    }

    public StringFilter amortizedCostInformation() {
        if (amortizedCostInformation == null) {
            setAmortizedCostInformation(new StringFilter());
        }
        return amortizedCostInformation;
    }

    public void setAmortizedCostInformation(StringFilter amortizedCostInformation) {
        this.amortizedCostInformation = amortizedCostInformation;
    }

    public BigDecimalFilter getAmortizationAmount() {
        return amortizationAmount;
    }

    public Optional<BigDecimalFilter> optionalAmortizationAmount() {
        return Optional.ofNullable(amortizationAmount);
    }

    public BigDecimalFilter amortizationAmount() {
        if (amortizationAmount == null) {
            setAmortizationAmount(new BigDecimalFilter());
        }
        return amortizationAmount;
    }

    public void setAmortizationAmount(BigDecimalFilter amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    public BigDecimalFilter getAmortizationRate() {
        return amortizationRate;
    }

    public Optional<BigDecimalFilter> optionalAmortizationRate() {
        return Optional.ofNullable(amortizationRate);
    }

    public BigDecimalFilter amortizationRate() {
        if (amortizationRate == null) {
            setAmortizationRate(new BigDecimalFilter());
        }
        return amortizationRate;
    }

    public void setAmortizationRate(BigDecimalFilter amortizationRate) {
        this.amortizationRate = amortizationRate;
    }

    public BigDecimalFilter getAccumulatedAmortizationAmount() {
        return accumulatedAmortizationAmount;
    }

    public Optional<BigDecimalFilter> optionalAccumulatedAmortizationAmount() {
        return Optional.ofNullable(accumulatedAmortizationAmount);
    }

    public BigDecimalFilter accumulatedAmortizationAmount() {
        if (accumulatedAmortizationAmount == null) {
            setAccumulatedAmortizationAmount(new BigDecimalFilter());
        }
        return accumulatedAmortizationAmount;
    }

    public void setAccumulatedAmortizationAmount(BigDecimalFilter accumulatedAmortizationAmount) {
        this.accumulatedAmortizationAmount = accumulatedAmortizationAmount;
    }

    public StringFilter getRecipe() {
        return recipe;
    }

    public Optional<StringFilter> optionalRecipe() {
        return Optional.ofNullable(recipe);
    }

    public StringFilter recipe() {
        if (recipe == null) {
            setRecipe(new StringFilter());
        }
        return recipe;
    }

    public void setRecipe(StringFilter recipe) {
        this.recipe = recipe;
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
        final ItemAssetDepreciationDetailCriteria that = (ItemAssetDepreciationDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(attribute, that.attribute) &&
            Objects.equals(inventoriesStorageId, that.inventoriesStorageId) &&
            Objects.equals(costInformation, that.costInformation) &&
            Objects.equals(amortizedCostInformation, that.amortizedCostInformation) &&
            Objects.equals(amortizationAmount, that.amortizationAmount) &&
            Objects.equals(amortizationRate, that.amortizationRate) &&
            Objects.equals(accumulatedAmortizationAmount, that.accumulatedAmortizationAmount) &&
            Objects.equals(recipe, that.recipe) &&
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
            attribute,
            inventoriesStorageId,
            costInformation,
            amortizedCostInformation,
            amortizationAmount,
            amortizationRate,
            accumulatedAmortizationAmount,
            recipe,
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
        return "ItemAssetDepreciationDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalAttribute().map(f -> "attribute=" + f + ", ").orElse("") +
            optionalInventoriesStorageId().map(f -> "inventoriesStorageId=" + f + ", ").orElse("") +
            optionalCostInformation().map(f -> "costInformation=" + f + ", ").orElse("") +
            optionalAmortizedCostInformation().map(f -> "amortizedCostInformation=" + f + ", ").orElse("") +
            optionalAmortizationAmount().map(f -> "amortizationAmount=" + f + ", ").orElse("") +
            optionalAmortizationRate().map(f -> "amortizationRate=" + f + ", ").orElse("") +
            optionalAccumulatedAmortizationAmount().map(f -> "accumulatedAmortizationAmount=" + f + ", ").orElse("") +
            optionalRecipe().map(f -> "recipe=" + f + ", ").orElse("") +
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
