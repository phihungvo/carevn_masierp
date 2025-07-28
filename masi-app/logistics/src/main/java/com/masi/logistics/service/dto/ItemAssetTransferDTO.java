package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.dto.WorkspaceDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemAssetTransfer} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemAssetTransferDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    private UUID inventoriesStorageId;

    private InventoriesStorageDTO inventoriesStorage;

    private UUID transactionTypeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TransactionTypeDTO transactionType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID itemCategoryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemCategoryDTO itemCategory;

    private ZonedDateTime transferDate;

    @Lob
    private String description;

    private String fromUnit;

    private UUID fromDepartmentId;

    private WorkspaceDTO fromWorkspace;

    private UUID toDepartmentId;

    private WorkspaceDTO toWorkspace;

    private UUID fromPersonId;

    private EmployeeDTO fromPerson;

    private UUID toPersonId;

    private EmployeeDTO toPerson;

    private String fromAddress;

    private String toAddress;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO createdByEmployee;

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

    private Collection<AssetTransferDetailsDTO> assetTransferDetailsDTOS = new ArrayList<>();

}
