package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ItemInfo.
 */
@Data
@Table("item_info")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemInfo implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("name")
    private String name;

    @Column("registration_number")
    private String registrationNumber;

    @Column("registration_date")
    private ZonedDateTime registrationDate;

    @Column("handover_number")
    private String handoverNumber;

    @Column("handover_date")
    private ZonedDateTime handoverDate;

    @Column("handover_by")
    private String handoverBy;

    @Column("handover_by_id")
    private UUID handoverById;

    @Column("user_name")
    private String userName;

    @Column("user_id")
    private UUID userId;

    @Column("user_position")
    private String userPosition;

    @Column("series_number")
    private String seriesNumber;

    @Column("usage_date")
    private ZonedDateTime usageDate;

    @Column("invoice_number")
    private String invoiceNumber;

    @Column("invoice_date")
    private ZonedDateTime invoiceDate;

    @Column("note")
    private String note;

    @Column("status")
    private String status;

    @Column("liquidation_date")
    private ZonedDateTime liquidationDate;

    @Column("unit")
    private UUID unit;

    @Column("year_of_use")
    private Integer yearOfUse;

    @Column("month_of_use")
    private Integer monthOfUse;

    @Column("warranty_period")
    private ZonedDateTime warrantyPeriod;

    @Column("manufacturer")
    private String manufacturer;

    @Column("is_made_in")
    private Boolean isMadeIn;

    @Column("specs")
    private String specs;

    @Column("removal_date")
    private ZonedDateTime removalDate;

    @Column("reason_for_removal")
    private String reasonForRemoval;

    @Column("attribute")
    private Json attribute;

    @Column("item_sub_category_id")
    private UUID itemSubCategoryId;

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

    @Column("inventory_storage_id")
    private UUID inventoryStorageId; ;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ItemInfo id(UUID id) {
        this.setId(id);
        return this;
    }

    public ItemInfo code(String code) {
        this.setCode(code);
        return this;
    }

    public ItemInfo name(String name) {
        this.setName(name);
        return this;
    }

    public ItemInfo registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public ItemInfo registrationDate(ZonedDateTime registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public ItemInfo handoverNumber(String handoverNumber) {
        this.setHandoverNumber(handoverNumber);
        return this;
    }

    public ItemInfo handoverDate(ZonedDateTime handoverDate) {
        this.setHandoverDate(handoverDate);
        return this;
    }

    public ItemInfo handoverBy(String handoverBy) {
        this.setHandoverBy(handoverBy);
        return this;
    }

    public ItemInfo userId(UUID userId) {
        this.setUserId(userId);
        return this;
    }

    public ItemInfo userPosition(String userPosition) {
        this.setUserPosition(userPosition);
        return this;
    }

    public ItemInfo seriesNumber(String seriesNumber) {
        this.setSeriesNumber(seriesNumber);
        return this;
    }

    public ItemInfo usageDate(ZonedDateTime usageDate) {
        this.setUsageDate(usageDate);
        return this;
    }

    public ItemInfo invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public ItemInfo invoiceDate(ZonedDateTime invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public ItemInfo note(String note) {
        this.setNote(note);
        return this;
    }

    public ItemInfo status(String status) {
        this.setStatus(status);
        return this;
    }

    public ItemInfo liquidationDate(ZonedDateTime liquidationDate) {
        this.setLiquidationDate(liquidationDate);
        return this;
    }

    public ItemInfo unit(UUID unit) {
        this.setUnit(unit);
        return this;
    }

    public ItemInfo yearOfUse(Integer yearOfUse) {
        this.setYearOfUse(yearOfUse);
        return this;
    }

    public ItemInfo monthOfUse(Integer monthOfUse) {
        this.setMonthOfUse(monthOfUse);
        return this;
    }

    public ItemInfo warrantyPeriod(ZonedDateTime warrantyPeriod) {
        this.setWarrantyPeriod(warrantyPeriod);
        return this;
    }

    public ItemInfo manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public ItemInfo isMadeIn(Boolean isMadeIn) {
        this.setIsMadeIn(isMadeIn);
        return this;
    }

    public ItemInfo specs(String specs) {
        this.setSpecs(specs);
        return this;
    }

    public ItemInfo removalDate(ZonedDateTime removalDate) {
        this.setRemovalDate(removalDate);
        return this;
    }

    public ItemInfo reasonForRemoval(String reasonForRemoval) {
        this.setReasonForRemoval(reasonForRemoval);
        return this;
    }

    public ItemInfo attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public ItemInfo isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ItemInfo createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ItemInfo createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public ItemInfo updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public ItemInfo updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public ItemInfo deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public ItemInfo deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public ItemInfo company(String company) {
        this.setCompany(company);
        return this;
    }

    public ItemInfo department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ItemInfo setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
