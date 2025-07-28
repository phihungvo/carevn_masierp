package com.masi.sale.service.dto;

import java.util.UUID;

public interface Reviewable {
     UUID getDocumentId();
     public void addReview(RequestApprovalDTO review);
}
