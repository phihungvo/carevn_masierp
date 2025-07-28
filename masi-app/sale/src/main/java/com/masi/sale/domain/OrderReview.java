package com.masi.sale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.sale.domain.enumeration.OrderReviewStatus;
import com.masi.sale.domain.enumeration.ReviewApproveSolution;
import com.masi.sale.service.dto.OrderReviewDTO;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A OrderReview.
 */
@Data
@Table("order_review")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderReview implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private OrderReviewStatus status;

    @Column("approval_sign_file")
    private String approvalStatusSignFile;


    @Column("approval_status_note")
    private String approvalStatusNote;

    @Column("approval_solution")
    private ReviewApproveSolution approvalSolution;

    private LocalDate awaitingDate;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"orderReviews"}, allowSetters = true)
    private Order order;

    @Column("order_id")
    private UUID orderId;



    // jhipster-needle-entity-add-field - JHipster will add fields here

    public OrderReview id(UUID id) {
        this.setId(id);
        return this;
    }

    public OrderReview status(OrderReviewStatus status) {
        this.setStatus(status);
        return this;
    }


    public OrderReview approvalStatusNote(String approvalStatusNote) {
        this.setApprovalStatusNote(approvalStatusNote);
        return this;
    }

    public OrderReview approvalSolution(ReviewApproveSolution approvalSolution) {
        this.setApprovalSolution(approvalSolution);
        return this;
    }

    public OrderReview awaitingDate(LocalDate awaitingDate) {
        this.setAwaitingDate(awaitingDate);
        return this;
    }

    public OrderReview lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public OrderReview createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public OrderReview employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public OrderReview setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public OrderReview order(Order order) {
        this.setOrder(order);
        return this;
    }

    public OrderReview orderId(UUID order) {
        this.orderId = order;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public OrderReviewDTO toDto() {
        OrderReviewDTO dto = new OrderReviewDTO();
        dto.setId(id);
        dto.setStatus(status);
        dto.setApprovalStatusSignFile(approvalStatusSignFile);
        dto.setApprovalStatusNote(approvalStatusNote);
        dto.setApprovalSolution(approvalSolution);
        dto.setAwaitingDate(awaitingDate);
        dto.setLastUpdated(lastUpdated);
        dto.setCreatedDate(createdDate);
        dto.setEmployeeId(employeeId);
        dto.setOrder(null);
        dto.setOrderId(orderId);
        return dto;
    }
}
