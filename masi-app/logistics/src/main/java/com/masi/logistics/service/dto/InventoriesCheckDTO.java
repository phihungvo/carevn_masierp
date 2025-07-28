package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.InventoriesCheckDetail;
import com.masi.logistics.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoriesCheck} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesCheckDTO implements Serializable {

    private UUID id;

    private String code;

    private ZonedDateTime checkDate;

    private UUID warehouseId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO warehouse;

    @Lob
    private String note;

    private Collection<RequestApprovalDTO> requestApprovals;

    private BigDecimal amountOfDifference;

    private UUID approver1;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO approver1Employee;

    private UUID approver2;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO approver2Employee;

    private UUID approver3;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO approver3Employee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachment;

    private List<InventoriesCheckDetailDTO> listInventoriesCheckDetail;

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
