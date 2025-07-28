package com.carevn.masi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
public class DocumentReviewMap<T> {
    private UUID documentId;
    private Collection<T> requestApprovals;

    public void addRequestApproval(T requestApproval) {
        if (requestApprovals == null) {
            requestApprovals = new ArrayList<>();
        }
        requestApprovals.add(requestApproval);
    }
}
