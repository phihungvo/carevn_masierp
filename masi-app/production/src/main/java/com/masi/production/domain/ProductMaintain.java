package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.ProductMaintainDTO;
import jakarta.validation.constraints.*;
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
 * A ProductMaintain.
 */
@Data
@Table("product_maintain")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductMaintain implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("product_batch_code")
    private String productBatchCode;

    @NotNull(message = "must not be null")
    @Column("product_batch_name")
    private String productBatchName;

    @NotNull(message = "must not be null")
    @Column("manufacture_date")
    private LocalDate manufactureDate;

    @NotNull(message = "must not be null")
    @Column("expired_date")
    private LocalDate expiredDate;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("last_updated_at")
    private ZonedDateTime lastUpdatedAt;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Column("product_package_id")
    private UUID productPackageId;

    @Transient
    @JsonIgnoreProperties(value = { "productMaintain" }, allowSetters = true)
    private ProductPackage productPackage;

    @Column("note")
    private String note;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProductMaintain id(UUID id) {
        this.setId(id);
        return this;
    }

    public ProductMaintain productBatchCode(String productBatchCode) {
        this.setProductBatchCode(productBatchCode);
        return this;
    }

    public ProductMaintain productBatchName(String productBatchName) {
        this.setProductBatchName(productBatchName);
        return this;
    }

    public ProductMaintain manufactureDate(LocalDate manufactureDate) {
        this.setManufactureDate(manufactureDate);
        return this;
    }

    public ProductMaintain expiredDate(LocalDate expiredDate) {
        this.setExpiredDate(expiredDate);
        return this;
    }

    public ProductMaintain isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public ProductMaintain createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ProductMaintain lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ProductMaintain setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ProductMaintainDTO toDto() {
        ProductMaintainDTO dto = new ProductMaintainDTO();
        dto.setId(this.id);
        dto.setProductBatchCode(this.productBatchCode);
        dto.setProductBatchName(this.productBatchName);
        dto.setManufactureDate(this.manufactureDate);
        dto.setExpiredDate(this.expiredDate);
        dto.setIsDeleted(this.isDeleted);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdatedAt(this.lastUpdatedAt);
        return dto;
    }

}
