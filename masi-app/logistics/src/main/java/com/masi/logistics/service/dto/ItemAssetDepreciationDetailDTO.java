package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemAssetDepreciationDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetDepreciationDetailDTO implements Serializable {

    private UUID id;

    private String code;

    private String attribute;

    @Lob
    private String name;

    private UUID inventoriesStorageId;
    private InventoriesStorageDTO inventoriesStorage;

    @Lob
    private String note;

    private String costInformation;

    private String amortizedCostInformation;

    private BigDecimal amortizationAmount;

    private BigDecimal amortizationRate;

    private BigDecimal accumulatedAmortizationAmount;

    private String recipe;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID itemAssetDepreciationId;

    private ItemAssetDepreciationDTO itemAssetDepreciation;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

}
