package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.service.dto.InventoriesCheckDetailDTO;
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
 * A InventoriesCheckDetail.
 */
@Data
@Table("inventories_check_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesCheckDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("inventories_check_id")
    private UUID inventoriesCheckId;

    @Column("code")
    private String code;

    @Column("item_id")
    private UUID itemId;

    @Column("system_quantity")
    private BigDecimal systemQuantity;

    @Column("actual_quantity")
    private BigDecimal actualQuantity;

    @Column("note")
    private String note;

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

    public InventoriesCheckDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public InventoriesCheckDetail inventoriesCheckId(UUID inventoriesCheckId) {
        this.setInventoriesCheckId(inventoriesCheckId);
        return this;
    }

    public InventoriesCheckDetail code(String code) {
        this.setCode(code);
        return this;
    }

    public InventoriesCheckDetail itemId(UUID itemId) {
        this.setItemId(itemId);
        return this;
    }

    public InventoriesCheckDetail systemQuantity(BigDecimal systemQuantity) {
        this.setSystemQuantity(systemQuantity);
        return this;
    }

    public InventoriesCheckDetail actualQuantity(BigDecimal actualQuantity) {
        this.setActualQuantity(actualQuantity);
        return this;
    }

    public InventoriesCheckDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public InventoriesCheckDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public InventoriesCheckDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public InventoriesCheckDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public InventoriesCheckDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public InventoriesCheckDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public InventoriesCheckDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public InventoriesCheckDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public InventoriesCheckDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public InventoriesCheckDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InventoriesCheckDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public InventoriesCheckDetailDTO toDto(){
        InventoriesCheckDetailDTO dto = new InventoriesCheckDetailDTO();
        dto.setId(this.getId());
        dto.setInventoriesCheckId(this.getInventoriesCheckId());
        dto.setCode(this.getCode());
        dto.setItemId(this.getItemId());
        dto.setSystemQuantity(this.getSystemQuantity());
        dto.setActualQuantity(this.getActualQuantity());
        dto.setNote(this.getNote());
        dto.setIsDeleted(this.getIsDeleted());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setCreatedBy(this.getCreatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setDeletedAt(this.getDeletedAt());
        dto.setDeletedBy(this.getDeletedBy());
        dto.setCompany(this.getCompany());
        dto.setDepartment(this.getDepartment());

        return dto;
    }
}
