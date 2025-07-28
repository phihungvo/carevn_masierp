package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoriesDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesDetailDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private UUID itemId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO item;

    private UUID inventoriesId;

    private BigDecimal quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;

    @Lob
    private String note;

    private String codeUom;

    private UUID uomId;

    private String uomName;

    private Float beforeItemInventory;

    private Float afterItemInventory;

    private BigDecimal costPrice;

    private Boolean isDeleted;

    private Integer vatRate;

    private UUID vatId;

    private BigDecimal vatAmount;

    private ZonedDateTime registerDate;

    private ZonedDateTime depreciationDate;

    private UUID departmentId;

    private Integer usageMonth;

    private String holder;

    private String depreciationAllocation;

    private String expenseAccount;

    private String costElements;

    private BigDecimal unitPrice;

    private Float ProteinPercentageApply;
    private Boolean isDefaultItem;


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
