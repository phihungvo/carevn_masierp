package com.masi.logistics.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.masi.logistics.domain.SupplierContractDetail} entity. This class is used
 * in {@link com.masi.logistics.web.rest.SupplierContractDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-contract-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContractDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private UUIDFilter contractId;

    private UUIDFilter supplyItemId;

    private UUIDFilter unitId;

    private IntegerFilter quantity;

    private StringFilter note;

    private UUIDFilter supplierContractId;

    private Boolean distinct;

    private BooleanFilter isDeleted;

    public SupplierContractDetailCriteria() {}

    public SupplierContractDetailCriteria(SupplierContractDetailCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.contractId = other.optionalContractId().map(UUIDFilter::copy).orElse(null);
        this.supplyItemId = other.optionalSupplyItemId().map(UUIDFilter::copy).orElse(null);
        this.unitId = other.optionalUnitId().map(UUIDFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.supplierContractId = other.optionalSupplierContractId().map(UUIDFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplierContractDetailCriteria copy() {
        return new SupplierContractDetailCriteria(this);
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

    public Optional<UUIDFilter> optionalContractId() {
        return Optional.ofNullable(contractId);
    }

    public UUIDFilter contractId() {
        if (contractId == null) {
            setContractId(new UUIDFilter());
        }
        return contractId;
    }

    public Optional<UUIDFilter> optionalSupplyItemId() {
        return Optional.ofNullable(supplyItemId);
    }

    public UUIDFilter supplyItemId() {
        if (supplyItemId == null) {
            setSupplyItemId(new UUIDFilter());
        }
        return supplyItemId;
    }

    public Optional<UUIDFilter> optionalUnitId() {
        return Optional.ofNullable(unitId);
    }

    public UUIDFilter unitId() {
        if (unitId == null) {
            setUnitId(new UUIDFilter());
        }
        return unitId;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public Optional<UUIDFilter> optionalSupplierContractId() {
        return Optional.ofNullable(supplierContractId);
    }

    public UUIDFilter supplierContractId() {
        if (supplierContractId == null) {
            setSupplierContractId(new UUIDFilter());
        }
        return supplierContractId;
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

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
    }

}
