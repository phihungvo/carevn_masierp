package com.masi.logistics.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateReview {
    private UUID documentId;
    private String approvedSign;
    private String approvedSignName;
    private String rejectNote;
    @NotNull
    private Boolean isApproved=false;
}
