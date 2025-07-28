package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.sale.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DeliverySchedule.
 */
@Data
@Table("delivery_schedule")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliverySchedule implements Serializable, Persistable<UUID> {

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

    @Column("contract_id")
    private UUID contractId;

    @Column("order_id")
    private UUID orderId;

    @Column("content")
    private String content;

    @Column("quantity")
    private Integer quantity;

    @Column("unit_id")
    private UUID unitId;

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
    private StatusEntity status;

    @Column("attachment")
    private Json attachment;

    @Column("attribute")
    private Json attribute;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

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

    public DeliverySchedule contractId(UUID contractId) {
        this.setContractId(contractId);
        return this;
    }

    public DeliverySchedule orderId(UUID orderId) {
        this.setOrderId(orderId);
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

    public DeliverySchedule attachment(Json attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public DeliverySchedule attribute(Json attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public DeliverySchedule isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public DeliverySchedule createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public DeliverySchedule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public DeliverySchedule updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public DeliverySchedule updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public DeliverySchedule deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public DeliverySchedule deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public DeliverySchedule company(String company) {
        this.setCompany(company);
        return this;
    }

    public DeliverySchedule department(String department) {
        this.setDepartment(department);
        return this;
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
