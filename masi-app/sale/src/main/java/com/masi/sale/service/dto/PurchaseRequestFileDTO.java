package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.PurchaseRequestFile} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseRequestFileDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private String filePath;

    private PurchaseRequestDTO purchaseRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseRequestFileDTO purchaseRequestFileDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseRequestFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseRequestFileDTO{" +
            "id='" + getId() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", purchaseRequest=" + getPurchaseRequest() +
            "}";
    }
}
