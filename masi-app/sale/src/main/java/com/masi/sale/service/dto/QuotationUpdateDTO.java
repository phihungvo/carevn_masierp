package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationUpdateDTO implements Serializable {

    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    private UUID customerId;

    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    private String company;

    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    private String updateBy;

    @NotNull
    private String name;

    private String description;

    private String deliveryLocation;
    private String deliveryLocationEn;
    private String priceType;

    private String priceTypeEn;

    private String materialCriteria;

    private String materialCriteriaEn;

    private ZonedDateTime deliveryDate;

    @NotNull
    private String packaging;

    @NotNull
    private String packagingEn;
    @NotNull
    private String minimumWeight;
    @NotNull
    private String paymentMethod;
    @NotNull
    private String paymentMethodEn;

    private HashSet<QuotationDetailCreateDTO> quotationDetails;

    public QuotationDTO toDto() {
        QuotationDTO quotationDTO = new QuotationDTO();
        quotationDTO.setName(name);
        quotationDTO.setCustomerId(this.customerId);
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
