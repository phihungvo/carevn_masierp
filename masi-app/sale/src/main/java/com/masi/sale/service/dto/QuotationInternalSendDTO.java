package com.masi.sale.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

import com.masi.sale.domain.enumeration.QuotationStatus;

import jakarta.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationInternalSendDTO implements Serializable {
    @NotNull(message = "must not be null")
    private UUID approverId;

    public QuotationDTO toDto(QuotationInternalSendDTO quotationInternalSendDTO, UUID id) {
        QuotationDTO quotationDTO = new QuotationDTO();
        quotationDTO.setId(id);
        quotationDTO.setStatus(QuotationStatus.WAITING_APPROVAL);
        quotationDTO.setApproverId(quotationInternalSendDTO.getApproverId());
        return quotationDTO;
    }
}
