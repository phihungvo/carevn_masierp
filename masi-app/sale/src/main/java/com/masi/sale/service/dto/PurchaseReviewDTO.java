package com.masi.sale.service.dto;

import com.masi.sale.domain.OrderReview;
import com.masi.sale.domain.PurchaseReview;
import com.masi.sale.domain.enumeration.PurchaseReviewStatus;
import com.masi.sale.domain.enumeration.PurchaseReviewStatus;
import com.masi.sale.domain.enumeration.ReviewApproveSolution;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.PurchaseReview} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseReviewDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private PurchaseReviewStatus status;

    private String approvalStatusFile;
    private String approvalStatusNote;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    private PurchaseRequestDTO purchaseRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseReviewDTO)) {
            return false;
        }

        PurchaseReviewDTO purchaseReviewDTO = (PurchaseReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

    public PurchaseReview applyReview(PurchaseReview review) {
        review.setLastUpdated(ZonedDateTime.now());
        if (PurchaseReviewStatus.APPROVED.equals(getStatus())) {
            if (approvalStatusFile == null)
                throw new BadRequestAlertException("Approval status sign is required", "orderReview",
                    "approvalStatusSignRequired");
            review.setStatus(getStatus());
            review.setApprovalStatusSignFile(getApprovalStatusFile());
            return review;
        }

        if (PurchaseReviewStatus.REJECTED.equals(getStatus())) {
            if (getApprovalStatusNote() == null)
                throw new BadRequestAlertException("Approval status note is required", "orderReview",
                    "approvalStatusNoteRequired");
            review.setStatus(getStatus());
            review.setApprovalStatusNote(getApprovalStatusNote());
        }

        return review;
    }
}
