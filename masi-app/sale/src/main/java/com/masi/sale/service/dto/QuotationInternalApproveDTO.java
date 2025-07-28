package com.masi.sale.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationInternalApproveDTO implements Serializable {
    private String approvalSignFile;
    private String approvalSignContentType;
    private String approvalSignFileName;
    public QuotationDTO toDto() {
        var quotationDto = new QuotationDTO();
        quotationDto.setFileId(approvalSignFile);
        quotationDto.setFileName(approvalSignFileName);
        return quotationDto;
    }
}
