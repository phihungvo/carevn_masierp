package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PurchaseDelivery.
 */
@Table("purchase_delivery")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class PurchaseDelivery implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;


    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("deliveried")
    private Float deliveried;

    @NotNull(message = "must not be null")
    @Column("waiting_delivery")
    private Float waitingDelivery;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "purchaseRequestFiles", "purchaseDeliveries", "purchaseReviews" }, allowSetters = true)
    private PurchaseRequest purchaseRequest;

    @Column("purchase_request_id")
    private UUID purchaseRequestId;

    @NotNull(message = "must not be null")
    @Column("updated_date")
    private ZonedDateTime updatedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PurchaseDelivery id(UUID id) {
        this.setId(id);
        return this;
    }


    public PurchaseDelivery deliveried(Float deliveried) {
        this.setDeliveried(deliveried);
        return this;
    }



    public PurchaseDelivery waitingDelivery(Float waitingDelivery) {
        this.setWaitingDelivery(waitingDelivery);
        return this;
    }



    public PurchaseDelivery createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PurchaseDelivery setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
        this.purchaseRequestId = purchaseRequest != null ? purchaseRequest.getId() : null;
    }

    public PurchaseDelivery purchaseRequest(PurchaseRequest purchaseRequest) {
        this.setPurchaseRequest(purchaseRequest);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseDelivery)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchaseDelivery) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseDelivery{" +
            "id=" + getId() +
            ", deliveried=" + getDeliveried() +
            ", waitingDelivery=" + getWaitingDelivery() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
