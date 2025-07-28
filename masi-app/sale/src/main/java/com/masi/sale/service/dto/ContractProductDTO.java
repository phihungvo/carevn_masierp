package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.ContractProduct} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractProductDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID idContract;

    @NotNull(message = "must not be null")
    private UUID idProduct;

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
    private String productName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractProductDTO)) {
            return false;
        }

        ContractProductDTO contractProductDTO = (ContractProductDTO) o;
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
            ", idProduct='" + getIdProduct() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", proteinParameters='" + getProteinParameters() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
