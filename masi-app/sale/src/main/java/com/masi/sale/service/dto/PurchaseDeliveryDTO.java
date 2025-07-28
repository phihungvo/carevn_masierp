package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.PurchaseDelivery;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.PurchaseDelivery} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@Builder
@NoArgsConstructor@AllArgsConstructor
public class PurchaseDeliveryDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    @Min(value = 0, message = "must be greater than or equal to 0")
    private Float deliveried;

    private Float waitingDelivery;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private  UUID purchaseRequestId;

    @JsonIgnore
    private PurchaseRequestDTO purchaseRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseDeliveryDTO purchaseDeliveryDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseDeliveryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public PurchaseDelivery toEntity() {
        PurchaseDelivery purchaseDelivery = new PurchaseDelivery();
        purchaseDelivery.setId(id);
        purchaseDelivery.setDeliveried(deliveried);
        purchaseDelivery.setWaitingDelivery(waitingDelivery);
        purchaseDelivery.setCreatedAt(ZonedDateTime.now());
        purchaseDelivery.setPurchaseRequestId(purchaseRequestId);
        return purchaseDelivery;
    }
}
