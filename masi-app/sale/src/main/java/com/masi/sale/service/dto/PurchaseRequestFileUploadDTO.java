package com.masi.sale.service.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class PurchaseRequestFileUploadDTO implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID purchaseRequestId;
    @Lob
    private byte[] file;
    private String contentType;
    private String fileName;

}
