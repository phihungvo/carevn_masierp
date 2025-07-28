package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.SuppliesItem} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuppliesItemDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID idSuppliesRequest;

    @NotNull(message = "must not be null")
    private UUID idItem;

    private UUID idUom;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UomDTO uomDTO;

    private UUID suppliesId;

    @NotNull(message = "must not be null")
    private BigDecimal quantity;

    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String imageIds;

    private Collection<String> image;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = ItemDTO.class)
    private ItemDTO item;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = SuppliersDTO.class)
    private SuppliersDTO suppliers;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    private String bankInfo;

    private String note;

    private Double vat;

    private UUID vatId;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountAfterVat;

    public BigDecimal getTotalAmount() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(quantity);
    }

    public BigDecimal getTotalAmountAfterVat() {
        BigDecimal amount = getTotalAmount();
        Double vatPercentage = this.vat == null ? 0 : this.vat;
        return amount.add(amount.multiply(BigDecimal.valueOf(vatPercentage)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
    }

}
