package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractMaterialDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID idContract;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ContractDTO contract;

    private UUID idMaterial;

    private String nameMaterialNew;
    private String nameMaterialNewEn;

    private Double price;

    private Double quantity;

    private String unit;

    private String proteinParameters;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String materialName;

    private UUID orderId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderDTO order;

    private UUID itemId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO itemDTO;

    private UUID manufactureOrderId;


    private Float deliveredQuantity;

    private MaterialDTO material;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractMaterialDTO)) {
            return false;
        }

        ContractMaterialDTO contractProductDTO = (ContractMaterialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractProductDTO{" +
            "id='" + getId() + "'" +
            ", idContract='" + getIdContract() + "'" +
            ", idProduct='" + getIdMaterial() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", proteinParameters=" + getProteinParameters() +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
