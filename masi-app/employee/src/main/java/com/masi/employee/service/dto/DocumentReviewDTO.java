package com.masi.employee.service.dto;

import com.masi.employee.domain.RequestApprovalDetail;
import lombok.Data;

import java.util.UUID;
@Data
public class DocumentReviewDTO {
    private UUID id;
    private String approvedSign;
    private String approvedSignName;
    private String rejectNote;
    private Boolean isApproved;

    public RequestApprovalDetail applyTO(RequestApprovalDetail entity) {
        if (isApproved != null && isApproved) {
            entity.setApprovedSign(approvedSign);
            entity.setApprovedSignName(approvedSignName);
        } else {
            entity.setRejectNote(rejectNote);
        }
        entity.setIsApproved(isApproved);
        return entity;
    }
}
