package com.masi.production.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.enumeration.ProductPackageStatus;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ProductPackage;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.ProductPackage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ProductPackageDTO implements Serializable {

    public static final Float DEFAULT_KG_PER_UNIT = 50f;

    private UUID id;

    @NotNull(message = "must not be null")
    private String packageCode;

    @NotNull(message = "must not be null")
    private Float quantity;

//    @NotNull(message = "must not be null")
    private String unit = UUID.randomUUID().toString();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomDTO uomDTO;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkOrderDTO workOrder;

    private UUID workOrderId;

    private UUID manufactureOrderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ManufactureOrderDTO manufactureOrder;

    private UUID materialId;

    private Boolean isSew;

    private ProductPackageStatus status;

    private Float weight;

    private UUID packageBy;

    private ZonedDateTime packageAt;

    private String note;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO packageByEmployee;

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
        calculateWeight();
    }

    private void calculateWeight() {
        var weightValue = 0f;
        if (this.quantity != null) {
            weightValue = this.quantity * DEFAULT_KG_PER_UNIT;
        }
        this.weight = weightValue;
    }

    public ProductPackage toEntity() {
        ProductPackage productPackage = new ProductPackage();
        productPackage.setId(this.id);
        productPackage.setPackageCode(this.packageCode);
        productPackage.setQuantity(this.quantity);
        productPackage.setUnit(this.unit);
        productPackage.setIsDeleted(this.isDeleted);
        productPackage.setCreatedAt(this.createdAt);
        productPackage.setLastUpdatedAt(this.lastUpdatedAt);
        productPackage.setWorkOrderId(this.workOrderId);
        productPackage.setManufactureOrderId(this.manufactureOrderId);
        productPackage.setMaterialId(this.materialId);
        productPackage.setIsSew(this.isSew);
        productPackage.setStatus(this.status);
        productPackage.setWeight(this.weight);
        productPackage.setPackageBy(this.packageBy);
        productPackage.setPackageAt(this.packageAt);
        productPackage.setNote(this.note);
        return productPackage;
    }

    public void updateEntity(ProductPackage productPackage) {
        if (productPackage == null) {
            return;
        }
        productPackage.setPackageCode(this.packageCode);
        productPackage.setQuantity(this.quantity);
        productPackage.setUnit(this.unit);
        productPackage.setWorkOrderId(this.workOrderId);
        productPackage.setManufactureOrderId(this.manufactureOrderId);
        productPackage.setMaterialId(this.materialId);
        productPackage.setIsSew(this.isSew);
        productPackage.setStatus(this.status);
        productPackage.setWeight(this.weight);
        productPackage.setPackageBy(this.packageBy);
        productPackage.setPackageAt(this.packageAt);
        productPackage.setNote(this.note);
    }
}
