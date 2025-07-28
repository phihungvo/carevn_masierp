package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.enumeration.ItemTypeCategory;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemCategory} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCategoryDTO implements Serializable {

    private UUID id;

    private String code;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseTypeDTO warehouseType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Collection<ItemDTO> items;

    private UUID warehouseTypeId;

    private ItemTypeCategory itemTypeCategory;
}
