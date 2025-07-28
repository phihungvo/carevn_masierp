package com.masi.sale.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.sale.domain.OrderReview;
import com.masi.sale.domain.enumeration.OrderReviewStatus;
import com.masi.sale.domain.enumeration.ReviewApproveSolution;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.OrderReview} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderReviewDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    private OrderReviewStatus status;

    private String approvalStatusSignFile;

    private String approvalStatusNote;

    private ReviewApproveSolution approvalSolution;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private FileAttachmentDTO approvalSignFile;

    private LocalDate awaitingDate;

    private ZonedDateTime lastUpdated;

    private ZonedDateTime createdDate;

    private UUID employeeId;
    private EmployeeDTO employee;

    private OrderDTO order;

    private UUID orderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderReviewDTO)) {
            return false;
        }

        OrderReviewDTO orderReviewDTO = (OrderReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderReviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderReviewDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", approvalStatusNote='" + getApprovalStatusNote() + "'" +
            ", approvalSolution='" + getApprovalSolution() + "'" +
            ", awaitingDate='" + getAwaitingDate() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", employeeId='" + getEmployeeId() + "'" +
            ", order=" + getOrder() +
            "}";
    }

    public OrderReview applyReview(OrderReview orderReview) {
        orderReview.setLastUpdated(ZonedDateTime.now());
        if (OrderReviewStatus.APPROVED.equals(getStatus())) {
            if (getApprovalStatusSignFile() == null)
                throw new BadRequestAlertException("Approval status sign is required", "orderReview",
                    "approvalStatusSignRequired");
            orderReview.setStatus(getStatus());
            orderReview.setApprovalStatusSignFile(getApprovalStatusSignFile());
            return orderReview;
        }

        if (OrderReviewStatus.REJECTED.equals(getStatus())) {
            if (getApprovalStatusNote() == null)
                throw new BadRequestAlertException("Approval status note is required", "orderReview",
                    "approvalStatusNoteRequired");
            orderReview.setStatus(getStatus());
            orderReview.setApprovalStatusNote(getApprovalStatusNote());
        }

        if (OrderReviewStatus.REJECTED.equals(getStatus())) {
            if (getApprovalSolution() == null)
                throw new BadRequestAlertException("Approval solution is required", "orderReview",
                    "approvalSolutionRequired");
            orderReview.setApprovalSolution(getApprovalSolution());

        }

        if (ReviewApproveSolution.WAITING.equals(getApprovalSolution())) {
            if (getAwaitingDate() == null)
                throw new BadRequestAlertException("Awaiting date is required", "orderReview", "awaitingDateRequired");
            orderReview.setAwaitingDate(getAwaitingDate());
        }
        return orderReview;
    }
}
