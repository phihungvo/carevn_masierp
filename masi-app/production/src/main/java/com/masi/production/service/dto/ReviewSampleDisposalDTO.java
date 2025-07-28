package com.masi.production.service.dto;

import com.masi.production.domain.SampleDisposal;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ReviewSampleDisposalDTO implements Serializable {
    private UUID id;
    @NotNull(message = "must not be null")
    private Boolean reviewerApproved;

    private String reviewerNote;

    private String reviewerSignFile;

    public void applyUpdateTo(SampleDisposal entity) {
        entity.setReviewerApproved(this.reviewerApproved);
        if (this.reviewerApproved) {
            return;
        }
        entity.setReviewerNote(this.reviewerNote);
        entity.setReviewerSignFile(this.reviewerSignFile);
    }


}
