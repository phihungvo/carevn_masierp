package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.Uom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.SupplierContractDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierContractDetailDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private UUID supplierContractId;

    private UUID supplyItemId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {ItemDTO.class})
    private ItemDTO item;

    private UUID unitId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {Uom.class})
    private UomDTO unit;

    private BigDecimal price;

    private BigDecimal quantity;

    private String note;

    private UUID vatId;

    private Double vatRate;

    private BigDecimal vatAmount;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountAfterVat;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {SupplierContractDTO.class})
    private SupplierContractDTO supplierContract;

    public BigDecimal getVatAmount() {
        if (vatRate == null || totalAmount == null) {
            return BigDecimal.ZERO;
        }
        return totalAmount.multiply(BigDecimal.valueOf(vatRate / 100))
            .setScale(2, RoundingMode.HALF_UP);
    }

    // Getter for totalAmount (calculated as price * quantity)
    public BigDecimal getTotalAmount() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
    }

    // Getter for totalAmountAfterVat (calculated field)
    public BigDecimal getTotalAmountAfterVat() {
        return getTotalAmount().add(getVatAmount());
    }

}
