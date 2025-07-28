package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
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
 * A InventoriesCheck.
 */
@Data
@Table("inventories_check")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesCheck implements Serializable, Persistable<UUID> {

    public static final String ENTITY_NAME = "inventories_check";
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("check_date")
    private ZonedDateTime checkDate;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Transient
    private Warehouse warehouse;

    @Column("amount_of_difference")
    private BigDecimal amountOfDifference;

    @Column("note")
    private String note;

    @Column("approver_1")
    private UUID approver1;

    @Column("approver_2")
    private UUID approver2;

    @Column("approver_3")
    private UUID approver3;

    @Column("status")
    private StatusEntity status;

    @Column("attribute")
    private Json attribute;

    @Column("attachment")
    private Json attachment;

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

    public InventoriesCheck id(UUID id) {
        this.setId(id);
        return this;
    }

    public InventoriesCheck code(String code) {
        this.setCode(code);
        return this;
    }

    public InventoriesCheck checkDate(ZonedDateTime checkDate) {
        this.setCheckDate(checkDate);
        return this;
    }

    public InventoriesCheck warehouse(UUID warehouse) {
        this.setWarehouseId(warehouse);
        return this;
    }

    public InventoriesCheck note(String note) {
        this.setNote(note);
        return this;
    }

    public InventoriesCheck approver1(UUID approver1) {
        this.setApprover1(approver1);
        return this;
    }

    public InventoriesCheck approver2(UUID approver2) {
        this.setApprover2(approver2);
        return this;
    }

    public InventoriesCheck approver3(UUID approver3) {
        this.setApprover3(approver3);
        return this;
    }

    public InventoriesCheck status(StatusEntity status) {
        this.setStatus(status);
        return this;
    }

    public InventoriesCheck attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public InventoriesCheck isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InventoriesCheck createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InventoriesCheck createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InventoriesCheck updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InventoriesCheck updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InventoriesCheck deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InventoriesCheck deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InventoriesCheck company(String company) {
        this.setCompany(company);
        return this;
    }

    public InventoriesCheck department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InventoriesCheck setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
