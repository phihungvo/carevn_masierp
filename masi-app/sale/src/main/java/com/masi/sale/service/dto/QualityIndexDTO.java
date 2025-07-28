package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.QualityIndex} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualityIndexDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String value;

    @NotNull(message = "must not be null")
    private UUID orderId;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QualityIndexDTO)) {
            return false;
        }

        QualityIndexDTO qualityIndexDTO = (QualityIndexDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, qualityIndexDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QualityIndexDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", orderId='" + getOrderId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
