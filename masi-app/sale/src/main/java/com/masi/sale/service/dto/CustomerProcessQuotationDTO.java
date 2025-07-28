package com.masi.sale.service.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CustomerProcessQuotationDTO {

    private CustomerProcessQuotationStatus status;

    private String rejectNote;

    private String fileId;

    private String fileName;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    public enum CustomerProcessQuotationStatus {
        CUSTOMER_APPROVED,
        REJECTED,
    }
}
