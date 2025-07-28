package com.masi.logistics.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RequestApproval.
 */
@Data
@Table("request_approval")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestApproval implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_GROUP = "APPROVAL";

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id=UUID.randomUUID();

    @Column("index")
    private Integer index;

    @NotNull(message = "must not be null")
    @Column("document_id")
    private UUID documentId;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @Column("result")
    private Boolean result;

    @Column("approved_sign")
    private String approvedSign;

    @Column("approved_sign_name")
    private String approvedSignName;

    @Column("reject_note")
    private String rejectNote;

    @Column("company")
    private String company;

    @Column("department")
    private String department;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("group_request")
    private String groupRequest = DEFAULT_GROUP;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RequestApproval id(UUID id) {
        this.setId(id);
        return this;
    }

    public RequestApproval index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public RequestApproval documentId(UUID documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public RequestApproval employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public RequestApproval result(Boolean result) {
        this.setResult(result);
        return this;
    }

    public RequestApproval approvedSign(String approvedSign) {
        this.setApprovedSign(approvedSign);
        return this;
    }

    public RequestApproval approvedSignName(String approvedSignName) {
        this.setApprovedSignName(approvedSignName);
        return this;
    }

    public RequestApproval rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }

    public RequestApproval company(String company) {
        this.setCompany(company);
        return this;
    }

    public RequestApproval department(String department) {
        this.setDepartment(department);
        return this;
    }

    public RequestApproval isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public RequestApproval createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public RequestApproval createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public RequestApproval updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public RequestApproval updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public RequestApproval deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public RequestApproval deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public RequestApproval setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
