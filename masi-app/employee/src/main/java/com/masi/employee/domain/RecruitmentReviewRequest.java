package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.PositionEmployee;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import com.masi.employee.service.dto.RecruitmentReviewRequestDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Arrays;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RecruitmentReviewRequest.
 */
@Data
@Table("recruitment_review_request")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentReviewRequest implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("position")
    private PositionEmployee position;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @NotNull(message = "must not be null")
    @Column("request_id")
    private UUID requestId;

    @NotNull(message = "must not be null")
    @Column("company")
    private String company;

    @Column("approval_sign_file")
    private String approvalSignFile;

    @Column("reject_note")
    private String rejectNote;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("result")
    private Boolean result;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RecruitmentReviewRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public RecruitmentReviewRequest position(PositionEmployee position) {
        this.setPosition(position);
        return this;
    }

    public RecruitmentReviewRequest employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public RecruitmentReviewRequest requestId(UUID requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public RecruitmentReviewRequest company(String company) {
        this.setCompany(company);
        return this;
    }








    public RecruitmentReviewRequest rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }

    public RecruitmentReviewRequest createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public RecruitmentReviewRequest updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public RecruitmentReviewRequest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public RecruitmentReviewRequest result(Boolean result) {
        this.setResult(result);
        return this;
    }

    public RecruitmentReviewRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public RecruitmentReviewRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    /*    public RecruitmentReviewRequestDTO toDto() {
        RecruitmentReviewRequestDTO dto = new RecruitmentReviewRequestDTO();
        dto.setId(this.getId());
        dto.setPosition(this.getPosition());
        dto.setEmployeeId(this.getEmployeeId());
        dto.setRequestId(this.getRequestId());
        dto.setCompany(this.getCompany());
        dto.setApprovalSign(this.getApprovalSign());
        dto.setApprovalSignContentType(this.getApprovalSignContentType());
        dto.setRejectNote(this.getRejectNote());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setResult(this.getResult());
        dto.setIsDeleted(this.getIsDeleted());
        return dto;
    }*/

    public RecruitmentReviewRequestDTO toDto() {
        var dto = this.toBriefDto();
        dto.setApprovalSignFile(this.approvalSignFile);
        dto.setRejectNote(this.rejectNote);
        return dto;
    }

    public RecruitmentReviewRequestDTO toBriefDto() {
        RecruitmentReviewRequestDTO dto = new RecruitmentReviewRequestDTO();
        dto.setId(this.getId());
        dto.setPosition(this.getPosition());
        dto.setEmployeeId(this.getEmployeeId());
        dto.setRequestId(this.getRequestId());
        dto.setCompany(this.getCompany());

        dto.setResult(!StringUtils.isBlank(this.getApprovalSignFile()));

        if (this.getResult() != null) {
            dto.setResult(this.getResult());
        }
        dto.setApprovalSignFile(this.getApprovalSignFile());
        dto.setRejectNote(this.getRejectNote());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setUpdatedBy(this.getUpdatedBy());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setIsDeleted(this.getIsDeleted());
        return dto;
    }
}
