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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationGetListDTO {
    private String search;
    private Collection<QuotationStatus> status;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String auth;
}
