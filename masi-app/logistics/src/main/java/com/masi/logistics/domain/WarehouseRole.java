package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WarehouseRole.
 */
@Table("warehouse_role")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarehouseRole implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("role_id")
    private UUID roleId;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "warehouses", "warehouseRoles", "itemCategories" }, allowSetters = true)
    private WarehouseType warehouseType;

    @Column("warehouse_type_id")
    private UUID warehouseTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public WarehouseRole id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRoleId() {
        return this.roleId;
    }

    public WarehouseRole roleId(UUID roleId) {
        this.setRoleId(roleId);
        return this;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public WarehouseRole createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public WarehouseRole createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public WarehouseRole updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public WarehouseRole updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return this.deleteAt;
    }

    public WarehouseRole deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return this.deleteBy;
    }

    public WarehouseRole deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return this.company;
    }

    public WarehouseRole company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public WarehouseRole setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public WarehouseType getWarehouseType() {
        return this.warehouseType;
    }

    public void setWarehouseType(WarehouseType warehouseType) {
        this.warehouseType = warehouseType;
        this.warehouseTypeId = warehouseType != null ? warehouseType.getId() : null;
    }

    public WarehouseRole warehouseType(WarehouseType warehouseType) {
        this.setWarehouseType(warehouseType);
        return this;
    }

    public UUID getWarehouseTypeId() {
        return this.warehouseTypeId;
    }

    public void setWarehouseTypeId(UUID warehouseType) {
        this.warehouseTypeId = warehouseType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarehouseRole)) {
            return false;
        }
        return getId() != null && getId().equals(((WarehouseRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarehouseRole{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }
}
