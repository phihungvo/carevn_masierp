package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.domain.enumeration.Unit;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PurchaseRequest.
 */
@Table("purchase_request")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class PurchaseRequest implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("request_status")
    private PurchaseRequestStatus requestStatus;

    @NotNull(message = "must not be null")
    @Column("product_name")
    private String productName;

    @NotNull(message = "must not be null")
    @Column("unit")
    private Unit unit;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Float quantity;

    @NotNull(message = "must not be null")
    @Column("unit_price")
    private Float unitPrice;

    @NotNull(message = "must not be null")
    @Column("total_price")
    private Float totalPrice;

    @Column("supplier")
    private String supplier;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("create_date")
    private ZonedDateTime createDate;
    @Column("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Column("updated_date")
    private ZonedDateTime updatedDate;
    @Column("updated_by")
    private String updatedBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("company")
    private String company;




    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"purchaseRequest"}, allowSetters = true)
    private Set<PurchaseRequestFile> purchaseRequestFiles = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = {"purchaseRequest"}, allowSetters = true)
    private Set<PurchaseDelivery> purchaseDeliveries = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = {"purchaseRequest"}, allowSetters = true)
    private Set<PurchaseReview> purchaseReviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public PurchaseRequest requestStatus(PurchaseRequestStatus purchaseRequestStatus) {
        this.setRequestStatus(purchaseRequestStatus);
        return this;
    }


    public PurchaseRequest productName(String productName) {
        this.setProductName(productName);
        return this;
    }


    public PurchaseRequest unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }


    public PurchaseRequest quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }


    public PurchaseRequest unitPrice(Float unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }


    public PurchaseRequest totalPrice(Float totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }


    public PurchaseRequest supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }


    public PurchaseRequest note(String note) {
        this.setNote(note);
        return this;
    }


    public PurchaseRequest createDate(ZonedDateTime createDate) {
        this.setCreateDate(createDate);
        return this;
    }


    public PurchaseRequest updatedDate(ZonedDateTime updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }


    public PurchaseRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PurchaseRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setPurchaseRequestFiles(Set<PurchaseRequestFile> purchaseRequestFiles) {
        if (this.purchaseRequestFiles != null) {
            this.purchaseRequestFiles.forEach(i -> i.setPurchaseRequest(null));
        }
        if (purchaseRequestFiles != null) {
            purchaseRequestFiles.forEach(i -> i.setPurchaseRequest(this));
        }
        this.purchaseRequestFiles = purchaseRequestFiles;
    }

    public PurchaseRequest purchaseRequestFiles(Set<PurchaseRequestFile> purchaseRequestFiles) {
        this.setPurchaseRequestFiles(purchaseRequestFiles);
        return this;
    }

    public PurchaseRequest addPurchaseRequestFile(PurchaseRequestFile purchaseRequestFile) {
        this.purchaseRequestFiles.add(purchaseRequestFile);
        purchaseRequestFile.setPurchaseRequest(this);
        return this;
    }

    public PurchaseRequest removePurchaseRequestFile(PurchaseRequestFile purchaseRequestFile) {
        this.purchaseRequestFiles.remove(purchaseRequestFile);
        purchaseRequestFile.setPurchaseRequest(null);
        return this;
    }

    public Set<PurchaseDelivery> getPurchaseDeliveries() {
        return this.purchaseDeliveries;
    }

    public void setPurchaseDeliveries(Set<PurchaseDelivery> purchaseDeliveries) {
        if (this.purchaseDeliveries != null) {
            this.purchaseDeliveries.forEach(i -> i.setPurchaseRequest(null));
        }
        if (purchaseDeliveries != null) {
            purchaseDeliveries.forEach(i -> i.setPurchaseRequest(this));
        }
        this.purchaseDeliveries = purchaseDeliveries;
    }

    public PurchaseRequest purchaseDeliveries(Set<PurchaseDelivery> purchaseDeliveries) {
        this.setPurchaseDeliveries(purchaseDeliveries);
        return this;
    }

    public PurchaseRequest addPurchaseDelivery(PurchaseDelivery purchaseDelivery) {
        this.purchaseDeliveries.add(purchaseDelivery);
        purchaseDelivery.setPurchaseRequest(this);
        return this;
    }

    public PurchaseRequest removePurchaseDelivery(PurchaseDelivery purchaseDelivery) {
        this.purchaseDeliveries.remove(purchaseDelivery);
        purchaseDelivery.setPurchaseRequest(null);
        return this;
    }

    public Set<PurchaseReview> getPurchaseReviews() {
        return this.purchaseReviews;
    }

    public void setPurchaseReviews(Set<PurchaseReview> purchaseReviews) {
        if (this.purchaseReviews != null) {
            this.purchaseReviews.forEach(i -> i.setPurchaseRequest(null));
        }
        if (purchaseReviews != null) {
            purchaseReviews.forEach(i -> i.setPurchaseRequest(this));
        }
        this.purchaseReviews = purchaseReviews;
    }

    public PurchaseRequest purchaseReviews(Set<PurchaseReview> purchaseReviews) {
        this.setPurchaseReviews(purchaseReviews);
        return this;
    }

    public PurchaseRequest addPurchaseReview(PurchaseReview purchaseReview) {
        this.purchaseReviews.add(purchaseReview);
        purchaseReview.setPurchaseRequest(this);
        return this;
    }

    public PurchaseRequest removePurchaseReview(PurchaseReview purchaseReview) {
        this.purchaseReviews.remove(purchaseReview);
        purchaseReview.setPurchaseRequest(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchaseRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseRequest{" +
            "id=" + getId() +
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

    public PurchaseRequest id(UUID uuid) {
        this.id = uuid;
        return this;
    }

    public PurchaseRequestDTO toDTO() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setId(this.id);
        purchaseRequestDTO.setRequestStatus(this.requestStatus);
        purchaseRequestDTO.setProductName(this.productName);
        purchaseRequestDTO.setUnit(this.unit);
        purchaseRequestDTO.setQuantity(this.quantity);
        purchaseRequestDTO.setUnitPrice(this.unitPrice);
        purchaseRequestDTO.setTotalPrice(this.totalPrice);
        purchaseRequestDTO.setSupplier(this.supplier);
        purchaseRequestDTO.setNote(this.note);
        purchaseRequestDTO.setCreateDate(this.createDate);
        purchaseRequestDTO.setUpdatedDate(this.updatedDate);
        purchaseRequestDTO.setIsDeleted(this.isDeleted);
        return purchaseRequestDTO;
    }
}
