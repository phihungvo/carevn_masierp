package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A QuotationDetail.
 */
@Data
@Table("quotation_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("index")
    private Integer index;

    @Column("delivery_location")
    private String deliveryLocation;

    @Column("delivery_location_en")
    private String deliveryLocationEn;

    @Column("delivery_date")
    private ZonedDateTime deliveryDate;

    @Column("packaging")
    private String packaging;

    @Column("packaging_en")
    private String packagingEn;

    @Column("minimum_weight")
    private String minimumWeight;

    @Column("weight")
    private String weight;

    @Column("nitrogen_180_price")
    private String nitrogen180Price;

    @Column("nitrogen_150_price")
    private String nitrogen150Price;

    @Column("price")
    private String price;

    @Column("price_type")
    private String priceType;

    @Column("price_type_en")
    private String priceTypeEn;

    @Column("payment_method")
    private String paymentMethod;

    @Column("payment_method_en")
    private String paymentMethodEn;

    @Column("material_id")
    private UUID materialId;

    @Transient
    @JsonIgnoreProperties(value = { "material" }, allowSetters = true)
    private Material material;

    @Column("note")
    private String note;

    @Column("material_criteria")
    private String materialCriteria;

    @Column("material_criteria_en")
    private String materialCriteriaEn;

    @Column("company")
    private String company;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("updated_by")
    private String updatedBy;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_date")
    private ZonedDateTime deletedDate;

    @Column("deleted_by")
    private String deletedBy;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "quotationExports", "quotationDetails" }, allowSetters = true)
    private Quotation quotation;

    @Column("quotation_id")
    private UUID quotationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public QuotationDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public QuotationDetail deliveryLocation(String deliveryLocation) {
        this.setDeliveryLocation(deliveryLocation);
        return this;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public ZonedDateTime getDeliveryDate() {
        return this.deliveryDate;
    }

    public QuotationDetail deliveryDate(ZonedDateTime deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPackaging() {
        return this.packaging;
    }

    public QuotationDetail packaging(String packaging) {
        this.setPackaging(packaging);
        return this;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getMinimumWeight() {
        return this.minimumWeight;
    }

    public QuotationDetail minimumWeight(String minimumWeight) {
        this.setMinimumWeight(minimumWeight);
        return this;
    }

    public void setMinimumWeight(String minimumWeight) {
        this.minimumWeight = minimumWeight;
    }

    public String getWeight() {
        return this.weight;
    }

    public QuotationDetail weight(String weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPriceType() {
        return this.priceType;
    }

    public QuotationDetail priceType(String priceType) {
        this.setPriceType(priceType);
        return this;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public QuotationDetail paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getMaterialId() {
        return this.materialId;
    }

    public QuotationDetail materialId(UUID materialId) {
        this.setMaterialId(materialId);
        return this;
    }

    public void setMaterialId(UUID materialId) {
        this.materialId = materialId;
    }

    public String getNote() {
        return this.note;
    }

    public QuotationDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMaterialCriteria() {
        return this.materialCriteria;
    }

    public QuotationDetail materialCriteria(String materialCriteria) {
        this.setMaterialCriteria(materialCriteria);
        return this;
    }

    public void setMaterialCriteria(String materialCriteria) {
        this.materialCriteria = materialCriteria;
    }

    public String getCompany() {
        return this.company;
    }

    public QuotationDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public QuotationDetail lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public QuotationDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public QuotationDetail createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public QuotationDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public QuotationDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTime getDeletedDate() {
        return this.deletedDate;
    }

    public QuotationDetail deletedDate(ZonedDateTime deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public QuotationDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public QuotationDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Quotation getQuotation() {
        return this.quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
        this.quotationId = quotation != null ? quotation.getId() : null;
    }

    public QuotationDetail quotation(Quotation quotation) {
        this.setQuotation(quotation);
        return this;
    }

    public UUID getQuotationId() {
        return this.quotationId;
    }

    public void setQuotationId(UUID quotation) {
        this.quotationId = quotation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotationDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationDetail{" +
                "id=" + getId() +
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
