package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.ProductionStandardDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ProductionStandard.
 */
@Data
@Table("production_standard")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionStandard implements Serializable, Persistable<UUID> {
    public enum Status {
        NEW, CANCELED
    }

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    //    @NotNull(message = "must not be null")
    @Column("quantity")
    private Float quantity;

    @NotNull(message = "must not be null")
    @Column("production_powder_qty")
    private Float productionPowderQty;

    @NotNull(message = "must not be null")
    @Column("unit")
    private String unit;

    @NotNull(message = "must not be null")
    @Column("material_Id")
    private UUID materialId;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    @Column("due_date")
    private LocalDate dueDate;

    @NotNull(message = "must not be null")
    @Column("workspace")
    private String workspace;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("last_updated_at")
    private ZonedDateTime lastUpdatedAt;

    @Transient
    private boolean isPersisted;

    @Column("status")
    private String status = "NEW";

    @Column("note")
    private String note;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProductionStandard id(UUID id) {
        this.setId(id);
        return this;
    }

    public ProductionStandard name(String name) {
        this.setName(name);
        return this;
    }

    public ProductionStandard quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public ProductionStandard productionPowderQty(Float productionPowderQty) {
        this.setProductionPowderQty(productionPowderQty);
        return this;
    }

    public ProductionStandard unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public ProductionStandard startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public ProductionStandard dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public ProductionStandard workspace(String workspace) {
        this.setWorkspace(workspace);
        return this;
    }

    public ProductionStandard isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ProductionStandard createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProductionStandard lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProductionStandard setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ProductionStandardDTO toDto() {
        ProductionStandardDTO productionStandardDTO = new ProductionStandardDTO();
        productionStandardDTO.setId(this.id);
        productionStandardDTO.setName(this.name);
        if (Objects.nonNull(this.dueDate)) {
            productionStandardDTO.setZonedDueDate(this.dueDate.atStartOfDay(ZoneOffset.UTC).toInstant().atZone(ZoneId.of("UTC")));
        }
        productionStandardDTO.setDueDate(this.dueDate);
        if (Objects.nonNull(this.startDate)) {
            productionStandardDTO.setZonedStartDate(this.startDate.atStartOfDay(ZoneOffset.UTC).toInstant().atZone(ZoneId.of("UTC")));
        }
        productionStandardDTO.setStartDate(this.startDate);
        productionStandardDTO.setQuantity(this.quantity);
        productionStandardDTO.setProductionPowderQty(this.productionPowderQty);
        productionStandardDTO.setUnit(this.unit);
        productionStandardDTO.setWorkspace(this.workspace);
        productionStandardDTO.setIsDeleted(this.isDeleted);
        productionStandardDTO.setCreatedAt(this.createdAt);
        productionStandardDTO.setLastUpdatedAt(this.lastUpdatedAt);
        productionStandardDTO.setMaterialId(this.materialId);
        productionStandardDTO.setStatus(Objects.isNull(this.status) ? Status.NEW.name() : this.status);
        productionStandardDTO.setNote(this.note);
        productionStandardDTO.setCode(this.code);
//        this.get
//        productionStandardDTO.set
        return productionStandardDTO;
    }
}
