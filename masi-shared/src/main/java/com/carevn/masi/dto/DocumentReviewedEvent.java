package com.carevn.masi.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DocumentReviewedEvent implements Serializable {
    public static final String EVENT_NAME = DocumentReviewedEvent.class.getName();
    private String documentId;
    private String entityName;
    private Boolean isApproved;
}
