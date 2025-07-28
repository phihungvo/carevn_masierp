package com.masi.sale.service.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InternalProcessQuotationDTO {

    private InternalProcessQuotationStatus status;

    private String approvalSignFile;
    private String approvalSignName;
    private String rejectNote;

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


}
