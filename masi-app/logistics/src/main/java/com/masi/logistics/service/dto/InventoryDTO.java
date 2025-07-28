package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.Inventory} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private Integer quantity;

    private ZonedDateTime manufacture;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

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

    private UUID itemId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO item;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO warehouse;

    private UUID warehouseId;

    private LocalDate expireDate;

    private UUID orderId;

    private UUID packageId;

    private UUID manufactureId;

}
