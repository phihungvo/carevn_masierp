package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RequestApprovalDetail.
 */
@Table("request_approval_detail")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestApprovalDetail implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("index")
    private Integer index;

    @NotNull(message = "must not be null")
    @Column("document_id")
    private UUID documentId;

    @NotNull(message = "must not be null")
    @Column("employee_id")
    private UUID employeeId;

    @Column("is_approved")
    private Boolean isApproved;

    @Column("approved_sign")
    private String approvedSign;

    @Column("approved_sign_name")
    private String approvedSignName;

    @Column("reject_note")
    private String rejectNote;

    @Column("entity_name")
    private String entityName;

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

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public RequestApprovalDetail id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getIndex() {
        return this.index;
    }

    public RequestApprovalDetail index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public UUID getDocumentId() {
        return this.documentId;
    }

    public RequestApprovalDetail documentId(UUID documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public UUID getEmployeeId() {
        return this.employeeId;
    }

    public RequestApprovalDetail employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public RequestApprovalDetail isApproved(Boolean isApproved) {
        this.setIsApproved(isApproved);
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getApprovedSign() {
        return this.approvedSign;
    }

    public RequestApprovalDetail approvedSign(String approvedSign) {
        this.setApprovedSign(approvedSign);
        return this;
    }

    public void setApprovedSign(String approvedSign) {
        this.approvedSign = approvedSign;
    }

    public String getApprovedSignName() {
        return this.approvedSignName;
    }

    public RequestApprovalDetail approvedSignName(String approvedSignName) {
        this.setApprovedSignName(approvedSignName);
        return this;
    }

    public void setApprovedSignName(String approvedSignName) {
        this.approvedSignName = approvedSignName;
    }

    public String getRejectNote() {
        return this.rejectNote;
    }

    public RequestApprovalDetail rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public RequestApprovalDetail entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCompany() {
        return this.company;
    }

    public RequestApprovalDetail company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return this.department;
    }

    public RequestApprovalDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public RequestApprovalDetail isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public RequestApprovalDetail createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public RequestApprovalDetail createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public RequestApprovalDetail updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public RequestApprovalDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public RequestApprovalDetail deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public ZonedDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public RequestApprovalDetail deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(ZonedDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public RequestApprovalDetail setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestApprovalDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((RequestApprovalDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestApprovalDetail{" +
            "id=" + getId() +
            ", index=" + getIndex() +
            ", documentId='" + getDocumentId() + "'" +
            ", employeeId='" + getEmployeeId() + "'" +
            ", isApproved='" + getIsApproved() + "'" +
            ", approvedSign='" + getApprovedSign() + "'" +
            ", approvedSignName='" + getApprovedSignName() + "'" +
            ", rejectNote='" + getRejectNote() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", company='" + getCompany() + "'" +
            ", department='" + getDepartment() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            "}";
    }
}
