package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ReleaseWarehouse;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleaseWarehouseDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID itemId;

    private String itemName;

    private UUID itemCategoryId;

    private String itemCategoryCode;

    private String itemCategoryName;

    private String itemCode;

    private UUID uomId;

    private String uomName;

    private UUID warehouseId;

    private String warehouseName;

    private UUID warehouseTypeId;

    private String warehouseTypeName;

    private Float percentProtein;

    private Float productionVolume;

    private Float quantity;

    private UUID orderId;

    private UUID manufactureOrderId;

    private UUID productionStandardId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    private Float productionQuantity;

    private LocalDate expireDate;

    private Float calculationQuantity;

    private UUID additiveMaterialChecklistId;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleaseWarehouseDTO)) {
            return false;
        }

        ReleaseWarehouseDTO releaseWarehouseDTO = (ReleaseWarehouseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, releaseWarehouseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseWarehouseDTO{" +
            "id='" + getId() + "'" +
            ", itemId='" + getItemId() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", itemCategoryId='" + getItemCategoryId() + "'" +
            ", itemCategoryCode='" + getItemCategoryCode() + "'" +
            ", itemCategoryName='" + getItemCategoryName() + "'" +
            ", itemCode='" + getItemCode() + "'" +
            ", uomId='" + getUomId() + "'" +
            ", uomName='" + getUomName() + "'" +
            ", warehouseId='" + getWarehouseId() + "'" +
            ", warehouseName='" + getWarehouseName() + "'" +
            ", warehouseTypeId='" + getWarehouseTypeId() + "'" +
            ", warehouseTypeName='" + getWarehouseTypeName() + "'" +
            ", percentProtein=" + getPercentProtein() +
            ", productionVolume=" + getProductionVolume() +
            ", orderId='" + getOrderId() + "'" +
            ", manufactureOrderId='" + getManufactureOrderId() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }

    // to entỉty function:
    public ReleaseWarehouse toEntity(){
        ReleaseWarehouse releaseWarehouse = new ReleaseWarehouse();
        releaseWarehouse.setId(this.id);
        releaseWarehouse.setItemId(this.itemId);
        releaseWarehouse.setItemName(this.itemName);
        releaseWarehouse.setItemCategoryId(this.itemCategoryId);
        releaseWarehouse.setItemCategoryCode(this.itemCategoryCode);
        releaseWarehouse.setItemCategoryName(this.itemCategoryName);
        releaseWarehouse.setItemCode(this.itemCode);
        releaseWarehouse.setUomId(this.uomId);
        releaseWarehouse.setUomName(this.uomName);
        releaseWarehouse.setWarehouseId(this.warehouseId);
        releaseWarehouse.setWarehouseName(this.warehouseName);
        releaseWarehouse.setWarehouseTypeId(this.warehouseTypeId);
        releaseWarehouse.setWarehouseTypeName(this.warehouseTypeName);
        releaseWarehouse.setPercentProtein(this.percentProtein);
        releaseWarehouse.setProductionVolume(this.productionVolume);
        releaseWarehouse.setQuantity(this.quantity);
        releaseWarehouse.setOrderId(this.orderId);
        releaseWarehouse.setManufactureOrderId(this.manufactureOrderId);
        releaseWarehouse.setProductionStandardId(this.productionStandardId);
        releaseWarehouse.setCompany(this.company);
        releaseWarehouse.setDepartment(this.department);
        releaseWarehouse.setIsDeleted(this.isDeleted);
        releaseWarehouse.setCreatedBy(this.createdBy);
        releaseWarehouse.setCreatedDate(this.createdDate);
        releaseWarehouse.setUpdatedBy(this.updatedBy);
        releaseWarehouse.setUpdatedAt(this.updatedAt);
        releaseWarehouse.setDeletedBy(this.deletedBy);
        releaseWarehouse.setDeletedAt(this.deletedAt);
        releaseWarehouse.setProductionQuantity(this.productionQuantity);
        releaseWarehouse.setExpireDate(this.expireDate);
        releaseWarehouse.setCalculationQuantity(this.calculationQuantity);
        releaseWarehouse.setAdditiveMaterialChecklistId(this.additiveMaterialChecklistId);
        return releaseWarehouse;
    }
}
