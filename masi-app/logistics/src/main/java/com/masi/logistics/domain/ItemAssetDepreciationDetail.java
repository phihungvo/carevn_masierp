package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemAssetDepreciationDetail.
 */
@Data
@Table("item_asset_depreciation_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetDepreciationDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("attribute")
    private String attribute;

    @Column("name")
    private String name;

    @Column("inventories_storage_id")
    private UUID inventoriesStorageId;

    @Transient
    private InventoriesStorage inventoriesStorage;


    @Column("item_asset_depreciation_id")
    private UUID itemAssetDepreciationId;

    @Column("note")
    private String note;

    @Column("cost_information")
    private String costInformation;

    @Column("amortized_cost_information")
    private String amortizedCostInformation;

    @Column("amortization_amount")
    private BigDecimal amortizationAmount;

    @Column("amortization_rate")
    private BigDecimal amortizationRate;

    @Column("accumulated_amortization_amount")
    private BigDecimal accumulatedAmortizationAmount;

    @Column("recipe")
    private String recipe;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ItemAssetDepreciationDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemAssetDepreciationDetail code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemAssetDepreciationDetail attribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemAssetDepreciationDetail name(String name) {
        this.setName(name);
        return this;
    }

    public ItemAssetDepreciationDetail inventoriesStorageId(UUID inventoriesStorageId) {
        this.setInventoriesStorageId(inventoriesStorageId);
        return this;
    }

    public ItemAssetDepreciationDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public ItemAssetDepreciationDetail costInformation(String costInformation) {
        this.setCostInformation(costInformation);
        return this;
    }

    public ItemAssetDepreciationDetail amortizedCostInformation(String amortizedCostInformation) {
        this.setAmortizedCostInformation(amortizedCostInformation);
        return this;
    }

    public ItemAssetDepreciationDetail amortizationAmount(BigDecimal amortizationAmount) {
        this.setAmortizationAmount(amortizationAmount);
        return this;
    }

    public ItemAssetDepreciationDetail amortizationRate(BigDecimal amortizationRate) {
        this.setAmortizationRate(amortizationRate);
        return this;
    }

    public ItemAssetDepreciationDetail accumulatedAmortizationAmount(BigDecimal accumulatedAmortizationAmount) {
        this.setAccumulatedAmortizationAmount(accumulatedAmortizationAmount);
        return this;
    }

    public ItemAssetDepreciationDetail recipe(String recipe) {
        this.setRecipe(recipe);
        return this;
    }

    public ItemAssetDepreciationDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemAssetDepreciationDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemAssetDepreciationDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemAssetDepreciationDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemAssetDepreciationDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemAssetDepreciationDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemAssetDepreciationDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemAssetDepreciationDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemAssetDepreciationDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemAssetDepreciationDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
