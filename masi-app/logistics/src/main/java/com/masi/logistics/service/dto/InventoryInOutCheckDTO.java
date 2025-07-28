package com.masi.logistics.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoryInOutCheck} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryInOutCheckDTO implements Serializable {

    private UUID id;

    private String code;

    private UUID transactionInId;

    private String transactionInCode;

    private UUID transactionOutId;

    private String transactionOutCode;

    private ZonedDateTime createAt;

    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private String department;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getTransactionInId() {
        return transactionInId;
    }

    public void setTransactionInId(UUID transactionInId) {
        this.transactionInId = transactionInId;
    }

    public String getTransactionInCode() {
        return transactionInCode;
    }

    public void setTransactionInCode(String transactionInCode) {
        this.transactionInCode = transactionInCode;
    }

    public UUID getTransactionOutId() {
        return transactionOutId;
    }

    public void setTransactionOutId(UUID transactionOutId) {
        this.transactionOutId = transactionOutId;
    }

    public String getTransactionOutCode() {
        return transactionOutCode;
    }

    public void setTransactionOutCode(String transactionOutCode) {
        this.transactionOutCode = transactionOutCode;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryInOutCheckDTO)) {
            return false;
        }

        InventoryInOutCheckDTO inventoryInOutCheckDTO = (InventoryInOutCheckDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryInOutCheckDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryInOutCheckDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", transactionInId='" + getTransactionInId() + "'" +
            ", transactionInCode='" + getTransactionInCode() + "'" +
            ", transactionOutId='" + getTransactionOutId() + "'" +
            ", transactionOutCode='" + getTransactionOutCode() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }
}
