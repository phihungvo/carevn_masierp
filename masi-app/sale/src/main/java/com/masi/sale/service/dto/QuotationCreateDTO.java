package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationCreateDTO implements Serializable {

    @NotNull
    private String name;

    private String description;

    private String deliveryLocation;
    private String deliveryLocationEn;

    private ZonedDateTime deliveryDate;

    private String packaging;
    private String packagingEn;

    private String minimumWeight;

    private String paymentMethod;
    private String paymentMethodEn;

    private String priceType;

    private String priceTypeEn;

    private String materialCriteria;

    private String materialCriteriaEn;

    private HashSet<QuotationDetailCreateDTO> quotationDetails;

    private UUID customerId;

    public QuotationDTO toDto() {
        QuotationDTO quotationDTO = new QuotationDTO();
        quotationDTO.setName(name);
        quotationDTO.setDescription(description);
        quotationDTO.setDeliveryDate(this.deliveryDate);
        quotationDTO.setDeliveryLocation(this.deliveryLocation);
        quotationDTO.setDeliveryLocationEn(this.deliveryLocationEn);
        quotationDTO.setPackaging(this.packaging);
        quotationDTO.setPackagingEn(this.packagingEn);
        quotationDTO.setPaymentMethod(this.paymentMethod);
        quotationDTO.setPaymentMethodEn(this.paymentMethodEn);
        quotationDTO.setMinimumWeight(this.minimumWeight);
        quotationDTO.setPriceType(this.priceType);
        quotationDTO.setPriceTypeEn(this.priceTypeEn);
        quotationDTO.setMaterialCriteria(this.materialCriteria);
        quotationDTO.setMaterialCriteriaEn(this.materialCriteriaEn);
        quotationDTO.setCustomerId(this.customerId);
        return quotationDTO;
    }

    public ArrayList<QuotationDetailDTO> toListDetail(UUID id) {
        var listDetail = new ArrayList<QuotationDetailDTO>();
        for (QuotationDetailCreateDTO detail : quotationDetails) {
            detail.setQuotationId(id);
            listDetail.add(detail.toDto());
        }
        return listDetail;
    }
}
