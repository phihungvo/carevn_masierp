package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DeliverySchedule.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("delivery_schedule")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliverySchedule extends AbstractAuditingEntity<UUID> implements Serializable, Persistable<UUID> {
    public enum Status {
        PENDING, DELIVERED, CANCELLED, WAITING_APPROVAL, APPROVED, REJECTED, NEW
    }

    public static final String ENTITY_NAME = "Delivery_Schedule".toUpperCase();
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("code")
    private String code;

    @Column("delivery_date")
    private LocalDate deliveryDate;

    @Column("expected_receive_date")
    private LocalDate expectedReceiveDate;

    @Column("order_id")
    private UUID orderId;

    @Column("content")
    private String content;

    @Column("quantity")
    private Integer quantity;

    @Column("unit_id")
    private UUID unitId;

    @Transient
    private Uom unit;

    @Column("price")
    private BigDecimal price;

    @Column("total")
    private BigDecimal total;

    @Column("payment_method")
    private String paymentMethod;

    @Column("receiver_name")
    private String receiverName;

    @Column("delivery_location")
    private String deliveryLocation;

    @Column("note")
    private String note;

    @Column("type")
    private String type;

    @Column("status")
    private Status status;


    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DeliverySchedule id(UUID id) {
        this.setId(id);
        return this;
    }

    public DeliverySchedule code(String code) {
        this.setCode(code);
        return this;
    }

    public DeliverySchedule deliveryDate(LocalDate deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public DeliverySchedule expectedReceiveDate(LocalDate expectedReceiveDate) {
        this.setExpectedReceiveDate(expectedReceiveDate);
        return this;
    }



    public DeliverySchedule content(String content) {
        this.setContent(content);
        return this;
    }

    public DeliverySchedule quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public DeliverySchedule unitId(UUID unitId) {
        this.setUnitId(unitId);
        return this;
    }

    public DeliverySchedule price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public DeliverySchedule total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public DeliverySchedule paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public DeliverySchedule receiverName(String receiverName) {
        this.setReceiverName(receiverName);
        return this;
    }

    public DeliverySchedule deliveryLocation(String deliveryLocation) {
        this.setDeliveryLocation(deliveryLocation);
        return this;
    }

    public DeliverySchedule note(String note) {
        this.setNote(note);
        return this;
    }

    public DeliverySchedule type(String type) {
        this.setType(type);
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DeliverySchedule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public DeliverySchedule createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public DeliverySchedule updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public DeliverySchedule updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public DeliverySchedule deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public ZonedDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public DeliverySchedule deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(ZonedDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public DeliverySchedule setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
