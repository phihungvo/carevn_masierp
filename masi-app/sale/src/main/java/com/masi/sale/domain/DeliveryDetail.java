package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DeliveryDetail.
 */
@Data
@Table("delivery_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("delivery_id")
    private UUID deliveryId;

    @Column("contract_material_id")
    private UUID contractMaterialId;

    @Column("quantity")
    private Integer quantity;

    @Column("uom_id")
    private UUID uomId;

    @Column("price")
    private BigDecimal price;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("attachment")
    private Json attachment;

    @Transient
    private UUID contractId;

    @Transient
    private boolean isPersisted;

    @Column("order_id")
    private UUID orderId;

    @Transient
    private Order order;

    @Column("delivery_date")
    private ZonedDateTime deliveryDate;

    @Column("actual_quantity")
    private Integer actualQuantity;

    @Column("difference_quantity")
    private Integer differenceQuantity;

    @Column("address")
    private String address;

    @Column("note")
    private String note;

    @Column("actual_delivery_date")
    private ZonedDateTime actualDeliveryDate;

    @Column("company")
    private String company;

    @Transient
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DeliveryDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public DeliveryDetail deliveryId(UUID deliveryId) {
        this.setDeliveryId(deliveryId);
        return this;
    }

    public DeliveryDetail contractMaterialId(UUID contractMaterialId) {
        this.setContractMaterialId(contractMaterialId);
        return this;
    }

    public DeliveryDetail quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public DeliveryDetail uomId(UUID uomId) {
        this.setUomId(uomId);
        return this;
    }

    public DeliveryDetail price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public DeliveryDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public DeliveryDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public DeliveryDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public DeliveryDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public DeliveryDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public DeliveryDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public DeliveryDetail attachment(Json attachment) {
        this.setAttachment(attachment);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public DeliveryDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
