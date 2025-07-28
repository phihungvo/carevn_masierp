package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.ProductPackageStatus;
import com.masi.production.service.dto.ProductPackageDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ProductPackage.
 */
@Table("product_package")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ProductPackage implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("package_code")
    private String packageCode;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Float quantity;

    @Column("weight")
    private Float weight;

    @NotNull(message = "must not be null")
    @Column("unit")
    private String unit;

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

    @Transient
    @JsonIgnoreProperties(value = { "workItem", "manufactureOrder", "productPackages" }, allowSetters = true)
    private WorkOrder workOrder;

    @Column("work_order_id")
    private UUID workOrderId;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Column("manufacture_order_id")
    private UUID manufactureOrderId;

    @Transient
    @JsonIgnoreProperties(value = { "productPackages", "workOrder" }, allowSetters = true)
    private ManufactureOrder manufactureOrder;

    @Column("material_id")
    private UUID materialId;

    @Column("is_sew")
    private Boolean isSew;

    @Column("status")
    private ProductPackageStatus status;

    @Column("package_by")
    private UUID packageBy;

    @Column("package_at")
    private ZonedDateTime packageAt;

    @Column("note")
    private String note;

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProductPackage setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ProductPackageDTO toDto() {
        ProductPackageDTO dto = new ProductPackageDTO();
        dto.setId(this.id);
        dto.setPackageCode(this.packageCode);
        dto.setQuantity(this.quantity);
        dto.setUnit(this.unit);
        dto.setIsDeleted(this.isDeleted);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdatedAt(this.lastUpdatedAt);
        dto.setWorkOrderId(this.workOrderId);
        dto.setManufactureOrderId(this.manufactureOrderId);
        dto.setMaterialId(this.materialId);
        dto.setIsSew(this.isSew);
        dto.setStatus(this.status);
        dto.setWeight(this.weight);
        dto.setPackageAt(this.packageAt);
        dto.setPackageBy(this.packageBy);
        dto.setNote(this.note);
        if(this.workOrder != null) {
            dto.setWorkOrder(this.workOrder.toDto());
        }
        if(this.manufactureOrder != null) {
            dto.setManufactureOrder(this.manufactureOrder.toDto());
        }
        return dto;
    }
}
