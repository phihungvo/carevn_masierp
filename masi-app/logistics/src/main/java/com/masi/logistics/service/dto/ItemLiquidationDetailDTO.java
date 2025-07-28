package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemLiquidationDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemLiquidationDetailDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private UUID inventoriesStorageId;
    private InventoriesStorageDTO inventoriesStorage;

    private String attribute;

    private String name;

    private ZonedDateTime liquidationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID itemLiquidationId;

    private ItemLiquidationDTO itemLiquidation;

    private String description;

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
