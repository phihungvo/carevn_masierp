package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.TypePageDepreciation;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemAssetDepreciation.
 */
@Data
@Table("item_asset_depreciation")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetDepreciation implements Serializable, Persistable<UUID> {

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

    @Column("status")
    private StatusEntity status;

    @Column("depreciation_date")
    private LocalDate depreciationDate;

    @Column("accounting_date")
    private LocalDate accountingDate;

    @Column("employee_id")
    private UUID employeeId;

    @Column("description")
    private String description;

    @Column("type_page_depreciation")
    private TypePageDepreciation typePageDepreciation;

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

    public ItemAssetDepreciation id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemAssetDepreciation code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemAssetDepreciation attribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemAssetDepreciation name(String name) {
        this.setName(name);
        return this;
    }

    public ItemAssetDepreciation status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public ItemAssetDepreciation depreciationDate(LocalDate depreciationDate) {
        this.setDepreciationDate(depreciationDate);
        return this;
    }

    public ItemAssetDepreciation accountingDate(LocalDate accountingDate) {
        this.setAccountingDate(accountingDate);
        return this;
    }

    public ItemAssetDepreciation employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public ItemAssetDepreciation description(String description) {
        this.setDescription(description);
        return this;
    }

    public ItemAssetDepreciation typePageDepreciation(TypePageDepreciation typePageDepreciation) {
        this.setTypePageDepreciation(typePageDepreciation);
        return this;
    }

    public ItemAssetDepreciation isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemAssetDepreciation createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemAssetDepreciation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemAssetDepreciation updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemAssetDepreciation updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemAssetDepreciation deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemAssetDepreciation deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemAssetDepreciation company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemAssetDepreciation department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemAssetDepreciation setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
