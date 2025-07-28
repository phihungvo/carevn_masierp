package com.masi.sale.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
public class DocumentReviewMap {
    private UUID documentId;
    private Collection<RequestApprovalDTO> requestApprovals;
    public void addRequestApproval(RequestApprovalDTO requestApproval) {
        if (requestApprovals == null) {
            requestApprovals = new ArrayList<>();
        }
        requestApprovals.add(requestApproval);
    }
}
