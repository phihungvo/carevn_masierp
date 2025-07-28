package com.masi.sale.service.dto;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.enumeration.QuotationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationDetailGetListDTO {

    private Collection<UUID> listId;

    private String search;
    private UUID materialId;
    private UUID quotationId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;
}
