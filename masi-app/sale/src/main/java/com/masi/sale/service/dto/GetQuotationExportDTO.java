package com.masi.sale.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.masi.sale.domain.QuotationExport} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GetQuotationExportDTO implements Serializable {

    private String pdfPath;

    private QuotationDTO quotation;

    private String CompanyId;

    private String Search;

}
