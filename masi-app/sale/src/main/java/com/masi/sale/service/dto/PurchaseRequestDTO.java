package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.domain.enumeration.Unit;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.PurchaseRequest} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseRequestDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    private PurchaseRequestStatus requestStatus;

    @NotNull(message = "must not be null")
    private String productName;

    @NotNull(message = "must not be null")
    private Unit unit;

    @NotNull(message = "must not be null")
    private Float quantity;

    @NotNull(message = "must not be null")
    private Float unitPrice;

    @NotNull(message = "must not be null")
    private Float totalPrice;

    private String supplier;

    private String note;


    private List<String> files;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<FileAttachmentDTO> purchaseRequestFiles;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PurchaseDeliveryDTO purchaseDelivery; // => capaj

    private ZonedDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseRequestDTO purchaseRequestDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseRequestDTO{" +
            "id='" + getId() + "'" +
            ", requestStatus='" + getRequestStatus() + "'" +
            ", productName='" + getProductName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", supplier='" + getSupplier() + "'" +
            ", note='" + getNote() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }

    public PurchaseRequest toEntity() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(this.id);
        purchaseRequest.setProductName(this.productName);
        purchaseRequest.setUnit(this.unit);
        purchaseRequest.setQuantity(this.quantity);
        purchaseRequest.setUnitPrice(this.unitPrice);
        purchaseRequest.setTotalPrice(this.totalPrice);
        purchaseRequest.setSupplier(this.supplier);
        purchaseRequest.setNote(this.note);
        purchaseRequest.setCreateDate(this.createDate);
        purchaseRequest.setUpdatedDate(ZonedDateTime.now());
        purchaseRequest.setIsDeleted(false);
        return purchaseRequest;
    }

    public PurchaseRequest applyChangesToEntity(PurchaseRequest purchaseRequest) {
        if (purchaseRequest != null) {
            purchaseRequest.setProductName(this.productName);
            purchaseRequest.setUnit(this.unit);
            purchaseRequest.setQuantity(this.quantity);
            purchaseRequest.setUnitPrice(this.unitPrice);
            purchaseRequest.setTotalPrice(this.totalPrice);
            purchaseRequest.setSupplier(this.supplier);
            purchaseRequest.setNote(this.note);
            purchaseRequest.setUpdatedDate(ZonedDateTime.now());
        }
        return purchaseRequest;
    }

}
