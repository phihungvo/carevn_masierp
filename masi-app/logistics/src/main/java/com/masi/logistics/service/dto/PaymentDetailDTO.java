package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.logistics.domain.PaymentDetail;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.PaymentDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDetailDTO implements Serializable {

    private UUID id;

    private UUID paymentRequestId;

    private UUID invoiceId;

    @JsonIgnoreProperties(value = {"requestApprovals", "incomingWarehouse", "suppliers", "paymentDetails"}, allowSetters = true)
    private IncomingInvoiceDTO incomingInvoice;

    public PaymentDetail toEntity() {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setId(this.id);
        paymentDetail.setPaymentRequestId(this.paymentRequestId);
        paymentDetail.setInvoiceId(this.invoiceId);
        return paymentDetail;
    }
}
