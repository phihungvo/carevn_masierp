package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoryCheck} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryCheckDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private UUID employeeId;

    private ZonedDateTime checkDate;

    private String notes;

    private Boolean result;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    private InventoryTransactionsDTO inventoryTransactions;

}
