package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.ReleaseWarehouseDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A ReleaseWarehouse.
 */
@Table("release_warehouse")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ReleaseWarehouse implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("item_id")
    private UUID itemId;

    @Column("item_name")
    private String itemName;

    @Column("item_category_id")
    private UUID itemCategoryId;

    @Column("item_category_code")
    private String itemCategoryCode;

    @Column("item_category_name")
    private String itemCategoryName;

    @Column("item_code")
    private String itemCode;

    @Column("uom_id")
    private UUID uomId;

    @Column("uom_name")
    private String uomName;

    @Column("warehouse_id")
    private UUID warehouseId;

    @Column("warehouse_name")
    private String warehouseName;

    @Column("warehouse_type_id")
    private UUID warehouseTypeId;

    @Column("warehouse_type_name")
    private String warehouseTypeName;

    @Column("percent_protein")
    private Float percentProtein;

    @Column("quantity")
    private Float quantity;

    @Column("production_volume")
    private Float productionVolume;

    @Column("order_id")
    private UUID orderId;

    @Column("manufacture_order_id")
    private UUID manufactureOrderId;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;


    @Column("production_standard_id")
    private UUID productionStandardId;

    @Column("additive_material_checklist_id")
    private UUID additiveMaterialChecklistId;

    @Transient
    private boolean isPersisted;

    @Column("production_quantity")
    private Float productionQuantity;

    @Column("expire_date")
    private LocalDate expireDate;

    @Column("calculation_quantity")
    private Float calculationQuantity;


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ReleaseWarehouse setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleaseWarehouse)) {
            return false;
        }
        return getId() != null && getId().equals(((ReleaseWarehouse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseWarehouse{" +
            "id=" + getId() +
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

    public ReleaseWarehouseDTO toDto(){
        var releaseWarehouseDTO = new ReleaseWarehouseDTO();
        // all field of ReleaseWarehouse to ReleaseWarehouseDTO
        releaseWarehouseDTO.setId(this.id);
        releaseWarehouseDTO.setItemId(this.itemId);
        releaseWarehouseDTO.setItemName(this.itemName);
        releaseWarehouseDTO.setItemCategoryId(this.itemCategoryId);
        releaseWarehouseDTO.setItemCategoryCode(this.itemCategoryCode);
        releaseWarehouseDTO.setItemCategoryName(this.itemCategoryName);
        releaseWarehouseDTO.setItemCode(this.itemCode);
        releaseWarehouseDTO.setUomId(this.uomId);
        releaseWarehouseDTO.setUomName(this.uomName);
        releaseWarehouseDTO.setWarehouseId(this.warehouseId);
        releaseWarehouseDTO.setWarehouseName(this.warehouseName);
        releaseWarehouseDTO.setWarehouseTypeId(this.warehouseTypeId);
        releaseWarehouseDTO.setWarehouseTypeName(this.warehouseTypeName);
        releaseWarehouseDTO.setPercentProtein(this.percentProtein);
        releaseWarehouseDTO.setProductionVolume(this.productionVolume);
        releaseWarehouseDTO.setQuantity(this.quantity);
        releaseWarehouseDTO.setOrderId(this.orderId);
        releaseWarehouseDTO.setManufactureOrderId(this.manufactureOrderId);
        releaseWarehouseDTO.setProductionStandardId(this.productionStandardId);
        releaseWarehouseDTO.setCompany(this.company);
        releaseWarehouseDTO.setDepartment(this.department);
        releaseWarehouseDTO.setIsDeleted(this.isDeleted);
        releaseWarehouseDTO.setCreatedBy(this.createdBy);
        releaseWarehouseDTO.setCreatedDate(this.createdDate);
        releaseWarehouseDTO.setUpdatedBy(this.updatedBy);
        releaseWarehouseDTO.setUpdatedAt(this.updatedAt);
        releaseWarehouseDTO.setDeletedBy(this.deletedBy);
        releaseWarehouseDTO.setDeletedAt(this.deletedAt);
        releaseWarehouseDTO.setProductionQuantity(this.productionQuantity);
        releaseWarehouseDTO.setExpireDate(this.expireDate);
        releaseWarehouseDTO.setCalculationQuantity(this.calculationQuantity);
        releaseWarehouseDTO.setAdditiveMaterialChecklistId(this.additiveMaterialChecklistId);
        return releaseWarehouseDTO;
    }
}
