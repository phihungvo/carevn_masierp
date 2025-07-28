package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.ProductionStandard;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.ProductionStandard} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionStandardDTO implements Serializable {

    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotNull(message = "must not be null")
    private String name;

    private Float quantity = 0f;

    private Float productionPowderQty;

    private String unit;

    private UomDTO uomDTO;

    private UUID materialId;

    private ItemDTO material;

    private LocalDate startDate;

    private ZonedDateTime zonedStartDate;

    private LocalDate dueDate;

    private ZonedDateTime zonedDueDate;

    private String workspace;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdatedAt;

    private Collection<ManufactureOrderDTO> manufactureOrderDTOS;

    @Schema(allowableValues = {"NEW", "CANCELED"})
    private String status;

    private String note;


}
