package com.masi.logistics.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformFormDetailDTO implements Serializable {

    private UUID id;

    private Integer quantity;

    private Integer quantityChange;

    private Double actualPrice;

    private Double basePrice;

    private UUID uomId;

    private String uomName;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private UniformDTO uniform;

    private UUID uniformId;

    private UniformReleaseDTO uniformRelease;

    private UUID uniformReleaseId;

    private UniformOrderDTO uniformOrder;

    private UUID uniformOrderId;

    private UUID uniformReturnId;

    private UUID uniformOrderStockId;

    private UniformOrderStockDTO uniformOrderStock;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniformFormDetailDTO)) {
            return false;
        }

        UniformFormDetailDTO uniformFormDetailDTO = (UniformFormDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniformFormDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniformFormDetailDTO{" +
            "id='" + getId() + "'" +
            ", quantity=" + getQuantity() +
            ", createAt='" + getCreateAt() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", updateBy='" + getUpdateBy() + "'" +
            ", deleteAt='" + getDeleteAt() + "'" +
            ", deleteBy='" + getDeleteBy() + "'" +
            ", company='" + getCompany() + "'" +
            ", uniform=" + getUniform() +
            ", uniformRelease=" + getUniformRelease() +
            ", uniformOrder=" + getUniformOrder() +
            "}";
    }
}
