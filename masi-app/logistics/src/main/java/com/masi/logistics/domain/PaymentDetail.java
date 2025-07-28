package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PaymentDetail.
 */
@Data
@Table("payment_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDetail extends AbstractAuditingEntity<UUID> implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("payment_request_id")
    private UUID paymentRequestId;

    @Column("invoice_id")
    private UUID invoiceId;

    @org.springframework.data.annotation.Transient
    private boolean isPersisted;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "paymentDetails" }, allowSetters = true)
    private PaymentRequest paymentRequest;

    @Transient
    @JsonIgnoreProperties(value = { "paymentDetails" }, allowSetters = true)
    private IncomingInvoice incomingInvoice;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public PaymentDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public PaymentDetail paymentRequestId(UUID paymentRequestId) {
        this.setPaymentRequestId(paymentRequestId);
        return this;
    }

    public PaymentDetail invoiceId(UUID invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    @org.springframework.data.annotation.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PaymentDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public PaymentDetail paymentRequest(PaymentRequest paymentRequest) {
        this.setPaymentRequest(paymentRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
