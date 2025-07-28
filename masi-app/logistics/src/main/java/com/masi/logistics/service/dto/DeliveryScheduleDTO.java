package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.AbstractAuditingEntity;
import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.RequestApproval;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A DTO for the {@link com.masi.logistics.domain.DeliverySchedule} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryScheduleDTO extends AuditingDto implements Serializable {

    private UUID id = UUID.randomUUID();

    private String code;

    private LocalDate deliveryDate;

    private LocalDate expectedReceiveDate;

    private UUID orderId;

    private String content;

    @JsonIgnore
    private Integer quantity;

    @JsonIgnore
    private UUID unitId;
    @JsonIgnore
    @Schema(allOf = {UomDTO.class})
    private UomDTO unit;

    @JsonIgnore
    private BigDecimal price;
    private BigDecimal total;

    private String paymentMethod;

    private String receiverName;

    private String deliveryLocation;

    private String note;

    private String type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DeliverySchedule.Status status = DeliverySchedule.Status.NEW;
    public Collection<CreateDeliveryDetailDto> createDeliveryDetails;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {RequestApprovalDTO.class})
    public Collection<RequestApprovalDTO> requestApprovals;
    public void addRequestApproval(RequestApprovalDTO requestApproval) {
        if (this.requestApprovals == null) {
            this.requestApprovals = new LinkedList<>();
        }
        this.requestApprovals.add(requestApproval);
    }
    public void applyChangeToEntity(DeliverySchedule entity) {
        entity.setDeliveryDate(this.getDeliveryDate());
        entity.setExpectedReceiveDate(this.getExpectedReceiveDate());
        entity.setOrderId(this.getOrderId());
        entity.setContent(this.getContent());
        entity.setQuantity(this.getQuantity());
        entity.setUnitId(this.getUnitId());
        entity.setPrice(this.getPrice());
        entity.setTotal(this.getTotal());
        entity.setPaymentMethod(this.getPaymentMethod());
        entity.setReceiverName(this.getReceiverName());
        entity.setDeliveryLocation(this.getDeliveryLocation());
        entity.setNote(this.getNote());
        entity.setType(this.getType());
    }
}
