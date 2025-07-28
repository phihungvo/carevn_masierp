package com.masi.sale.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.DeliveryDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDetailDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID deliveryId;

    private UUID contractMaterialId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MaterialDTO material;

    private Integer quantity;

    private UUID uomId;

    private ZonedDateTime deliveryDate;

    private ZonedDateTime actualDeliveryDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID contractId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ContractDTO contract;

    private BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attachment;

    private String note;

    private Integer actualQuantity;

    private String address;

    private Integer differenceQuantity;

    private UUID orderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderDTO order;

    private String company;
}
