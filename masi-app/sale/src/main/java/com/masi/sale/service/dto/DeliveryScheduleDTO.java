package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.DeliverySchedule} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryScheduleDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private LocalDate deliveryDate;

    private LocalDate expectedReceiveDate;

    private UUID contractId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ContractDTO contract;

    private UUID orderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderDTO order;

    private String content;

    private Integer quantity;

    private UUID unitId;

    private BigDecimal price;

    private BigDecimal total;

    private String paymentMethod;

    private String receiverName;

    private String deliveryLocation;

    private String note;

    private String type;

    private Json attachment;

    private Json attribute;

    private StatusEntity status;

    private List<DeliveryDetailDTO> deliveryDetail;

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
