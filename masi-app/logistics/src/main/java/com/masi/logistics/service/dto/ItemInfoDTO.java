package com.masi.logistics.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.ItemSubCategory;
import com.masi.logistics.service.IncludedAccessoriesDTO;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemInfo} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemInfoDTO implements Serializable {

    private UUID id;

    private String code;

    private String name;

    private String registrationNumber;

    private UUID itemSubCategoryId;

    private ItemSubCategoryDTO itemSubCategory;

    private ZonedDateTime registrationDate;

    private String handoverNumber;

    private ZonedDateTime handoverDate;

    private String handoverBy;

    private UUID handoverById;

    private String userName;

    private UUID userId;

    private String userPosition;

    private String seriesNumber;

    private ZonedDateTime usageDate;

    private String invoiceNumber;

    private ZonedDateTime invoiceDate;

    private UUID inventoryStorageId;

    private List<IncludedAccessoriesDTO> includedAccessories;

    @Lob
    private String note;

    private String status;

    private ZonedDateTime liquidationDate;

    private UUID unit;

    private Integer yearOfUse;

    private Integer monthOfUse;

    private ZonedDateTime warrantyPeriod;

    private String manufacturer;

    private Boolean isMadeIn;

    private String specs;

    private ZonedDateTime removalDate;

    private String reasonForRemoval;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

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
