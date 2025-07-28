package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.domain.enumeration.LiquidationReason;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.ItemLiquidation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemLiquidationDTO implements Serializable {

    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    private ZonedDateTime liquidationDate;

    @Lob
    private String description;

    @Lob
    private LiquidationReason reason;

    private Json personnelList;

    private List<ItemLiquidationDetailDTO> itemLiquidationDTODetails;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    private EmployeeDTO employee;

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

    private Collection<RequestApprovalDTO> requestApprovals;

}
