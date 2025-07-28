package com.masi.logistics.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.WarehouseRole} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseRoleDTO implements Serializable {

    private UUID id;

    private UUID roleId;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private WarehouseTypeDTO warehouseType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public WarehouseTypeDTO getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(WarehouseTypeDTO warehouseType) {
        this.warehouseType = warehouseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarehouseRoleDTO)) {
            return false;
        }

        WarehouseRoleDTO warehouseRoleDTO = (WarehouseRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warehouseRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarehouseRoleDTO{" +
            "id='" + getId() + "'" +
            ", roleId='" + getRoleId() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            ", warehouseType=" + getWarehouseType() +
            "}";
    }
}
