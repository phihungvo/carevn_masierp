package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ProductMaintain;
import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.ProductRouting;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.ProductMaintain} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ProductMaintainDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private String productBatchCode;

    @NotNull(message = "must not be null")
    private String productBatchName;

    @NotNull(message = "must not be null")
    private LocalDate manufactureDate;

    @NotNull(message = "must not be null")
    private LocalDate expiredDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdatedAt;

    private Collection<UUID> productPackageIds;

    private Collection<ProductPackageDTO> productPackageDTOS;

    private UUID productPackageId;

    private ProductPackageDTO productPackageDTO;

    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductMaintainDTO)) {
            return false;
        }

        ProductMaintainDTO productMaintainDTO = (ProductMaintainDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productMaintainDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductMaintainDTO{" +
            "id='" + getId() + "'" +
            ", productBatchCode='" + getProductBatchCode() + "'" +
            ", productBatchName='" + getProductBatchName() + "'" +
            ", manufactureDate='" + getManufactureDate() + "'" +
            ", expiredDate='" + getExpiredDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            "}";
    }

    public void applyUpdate(ProductMaintain productMaintain) {
        productMaintain.setProductBatchName(this.getProductBatchName());
        productMaintain.setProductBatchCode(this.getProductBatchCode());
        productMaintain.setManufactureDate(this.getManufactureDate());
        productMaintain.setExpiredDate(this.getExpiredDate());

    }

}
