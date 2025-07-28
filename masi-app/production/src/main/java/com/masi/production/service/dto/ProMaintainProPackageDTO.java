package com.masi.production.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ProMaintainProPackage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProMaintainProPackageDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private UUID productMaintainId;

    @NotNull(message = "must not be null")
    private String productPackage;

    private String company;

    private String department;

    private Boolean isDeleted;

    private String createdBy;

    private ZonedDateTime createdDate;

    private String updatedBy;

    private ZonedDateTime updatedAt;

    private String deletedBy;

    private ZonedDateTime deletedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductMaintainId() {
        return productMaintainId;
    }

    public void setProductMaintainId(UUID productMaintainId) {
        this.productMaintainId = productMaintainId;
    }

    public String getProductPackage() {
        return productPackage;
    }

    public void setProductPackage(String productPackage) {
        this.productPackage = productPackage;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public ZonedDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(ZonedDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProMaintainProPackageDTO)) {
            return false;
        }

        ProMaintainProPackageDTO proMaintainProPackageDTO = (ProMaintainProPackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proMaintainProPackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProMaintainProPackageDTO{" +
            "id='" + getId() + "'" +
            ", productMaintainId='" + getProductMaintainId() + "'" +
            ", productPackage='" + getProductPackage() + "'" +
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
}
