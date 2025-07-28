package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.QuotationDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDetailDTO implements Serializable {

    private UUID id;

    private Integer index;

    private String deliveryLocation;
    private String deliveryLocationEn;

    private ZonedDateTime deliveryDate;

    private String packaging;
    private String packagingEn;

    private String minimumWeight;

    private String weight;

    private String nitrogen180Price;

    private String nitrogen150Price;

    private String price;

    private String priceType;
    private String priceTypeEn;

    private String paymentMethod;
    private String paymentMethodEn;

    private UUID materialId;

    private UUID quotationId;

    private String note;

    private String materialCriteria;
    private String materialCriteriaEn;

    private String company;

    private ZonedDateTime lastUpdated;

    private String updatedBy;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private String createdBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private ZonedDateTime deletedDate;

    private String deletedBy;

    private MaterialDTO material;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getMinimumWeight() {
        return minimumWeight;
    }

    public void setMinimumWeight(String minimumWeight) {
        this.minimumWeight = minimumWeight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public void setMaterialId(UUID materialId) {
        this.materialId = materialId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMaterialCriteria() {
        return materialCriteria;
    }

    public void setMaterialCriteria(String materialCriteria) {
        this.materialCriteria = materialCriteria;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationDetailDTO)) {
            return false;
        }

        QuotationDetailDTO quotationDetailDTO = (QuotationDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationDetailDTO{" +
                "id='" + getId() + "'" +
                ", deliveryLocation='" + getDeliveryLocation() + "'" +
                ", deliveryDate='" + getDeliveryDate() + "'" +
                ", packaging='" + getPackaging() + "'" +
                ", minimumWeight=" + getMinimumWeight() +
                ", weight=" + getWeight() +
                ", priceType='" + getPriceType() + "'" +
                ", paymentMethod='" + getPaymentMethod() + "'" +
                ", materialId='" + getMaterialId() + "'" +
                ", note='" + getNote() + "'" +
                ", materialCriteria='" + getMaterialCriteria() + "'" +
                ", company='" + getCompany() + "'" +
                ", lastUpdated='" + getLastUpdated() + "'" +
                ", updatedBy='" + getUpdatedBy() + "'" +
                ", createdDate='" + getCreatedDate() + "'" +
                ", createdBy='" + getCreatedBy() + "'" +
                ", isDeleted='" + getIsDeleted() + "'" +
                ", deletedDate='" + getDeletedDate() + "'" +
                ", deletedBy='" + getDeletedBy() + "'" +
                "}";
    }
}
