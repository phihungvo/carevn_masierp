package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ProductRouting;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.ProductRouting} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductRoutingDTO implements Serializable {

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

//    @NotNull(message = "must not be null")
    private String name;

//    @NotNull(message = "must not be null")
    private Float quantity;

//    @NotNull(message = "must not be null")
    private String unit;

    private UomDTO uomDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private FactoryDTO factory;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StorageDTO storage;

//    @NotNull(message = "must not be null")
    private UUID factoryId;

//    @NotNull(message = "must not be null")
    private UUID storageId;

    private Float percentProtein;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO warehouseDTO;

    private UUID productMaintainId;

    private ManufactureOrderDTO manufactureOrder;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProductMaintainDTO productMaintainDTO;

    private ZonedDateTime warehouseDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    private String createdBy;

    public void applyUpdate(ProductRouting productRouting) {
        productRouting.setName(this.getName());
        productRouting.setFactoryId(this.getFactoryId());
        productRouting.setStorageId(this.getStorageId());
        productRouting.setQuantity(this.getQuantity());
        productRouting.setUnit(this.getUnit());
        productRouting.setProductMaintainId(this.getProductMaintainId());

    }

    public ProductRouting toEntity() {
        ProductRouting productRouting = new ProductRouting();
        productRouting.setId(this.getId());
        productRouting.setName(this.getName());
        productRouting.setFactoryId(this.getFactoryId());
        productRouting.setStorageId(this.getStorageId());
        productRouting.setQuantity(this.getQuantity());
        productRouting.setUnit(this.getUnit());
        productRouting.setProductMaintainId(this.getProductMaintainId());
        productRouting.setIsActive(this.getIsActive());
        productRouting.setCreatedAt(this.getCreatedAt());
        productRouting.setLastUpdatedAt(this.getLastUpdatedAt());
        productRouting.setWarehouseDate(this.getWarehouseDate());
        return productRouting;
    }
}
