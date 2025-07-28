package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.DeliveryDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDetailDTO implements Serializable {

    private UUID id;

    private UUID deliveryId;

    private LocalDate deliveryDate;

    private LocalDate actualDeliveryDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DeliveryScheduleDTO deliverySchedule;

    private UUID contractMaterialId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO contractMaterial;

    private Integer quantity;

    private BigDecimal contractQuantity;

    private UUID uomId;

    private BigDecimal price;

    private BigDecimal actualQuantity;

    private String address;

    @Lob
    private String note;

    ////
    ////
    ////Difference quantity between quantity and actual quantity by expected quantity subtract actual quantity
    ////
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal differenceQuantity;

    private UUID supplierContractId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SupplierContractDTO supplierContract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    private UUID contractDetailId;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateDifferenceQuantity();
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
        calculateDifferenceQuantity();
    }

    private void calculateDifferenceQuantity() {
        if (this.quantity != null && this.actualQuantity != null) {
            this.differenceQuantity = BigDecimal.valueOf(this.quantity).subtract(this.actualQuantity);
        }
    }

    public BigDecimal getContractQuantity() {
        return Optional
            .ofNullable(calculateContractQuantity())
            .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateContractQuantity() {
        if (supplierContract != null
            && supplierContract.getSupplierContractDetails() != null
            && !supplierContract.getSupplierContractDetails().isEmpty()) {

            return supplierContract.getSupplierContractDetails().stream()
                .filter(Objects::nonNull)
                .filter(supplierContractDetailDTO -> supplierContractDetailDTO.getSupplyItemId().equals(contractMaterialId))
                .map(SupplierContractDetailDTO::getQuantity)
                .findFirst()
                .orElse(BigDecimal.ZERO); // Sử dụng orElse để trả về giá trị mặc định
        }

        return BigDecimal.ZERO;
    }


}
