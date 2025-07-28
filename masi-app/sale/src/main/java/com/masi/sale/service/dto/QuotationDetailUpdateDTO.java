package com.masi.sale.service.dto;

import com.masi.sale.domain.enumeration.QuotationStatus;

import jakarta.annotation.Nullable;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.Quotation} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationDetailUpdateDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private String deliveryLocation;
    private String deliveryLocationEn;

    private ZonedDateTime deliveryDate;

    private String packaging;
    private String packagingEn;

    private String minimumWeight;

    private String weight;

    private String nitrogen180Price;

    private String nitrogen150Price;

    private String price;

    private String priceType;
    private String priceTypeEn;

    private String paymentMethod;
    private String paymentMethodEn;

    private UUID materialId;

    private String note;

    private String materialCriteria;
    private String materialCriteriaEn;

    private Integer index;

    public QuotationDetailDTO toDto() {
        var quotationDetailDTO = new QuotationDetailDTO();
        quotationDetailDTO.setId(id);
        quotationDetailDTO.setIndex(this.index);
        quotationDetailDTO.setDeliveryLocation(deliveryLocation);
        quotationDetailDTO.setDeliveryLocationEn(deliveryLocationEn);
        quotationDetailDTO.setDeliveryDate(deliveryDate);
        quotationDetailDTO.setPackaging(packaging);
        quotationDetailDTO.setPackagingEn(packagingEn);
        quotationDetailDTO.setMinimumWeight(minimumWeight);
        quotationDetailDTO.setWeight(weight);
        quotationDetailDTO.setPriceType(priceType);
        quotationDetailDTO.setPriceTypeEn(priceTypeEn);
        quotationDetailDTO.setPaymentMethod(paymentMethod);
        quotationDetailDTO.setPaymentMethodEn(paymentMethodEn);
        quotationDetailDTO.setMaterialId(materialId);
        quotationDetailDTO.setNote(note);
        quotationDetailDTO.setMaterialCriteria(materialCriteria);
        quotationDetailDTO.setMaterialCriteria(materialCriteriaEn);
        quotationDetailDTO.setNitrogen150Price(nitrogen150Price);
        quotationDetailDTO.setNitrogen180Price(nitrogen180Price);
        quotationDetailDTO.setPrice(price);
        // quotationDetailDTO.setQuotationId(quotationId);
        return quotationDetailDTO;
    }
}
