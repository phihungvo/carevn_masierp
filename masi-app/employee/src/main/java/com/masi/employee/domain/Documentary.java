package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.DocumentaryGroup;
import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.domain.enumeration.DocumentaryType;
import com.masi.employee.service.dto.DocumentaryDTO;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Documentary.
 */
@Table("documentary")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class Documentary implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("document_number")
    private String documentNumber;

    @NotNull(message = "must not be null")
    @Column("date_start")
    private LocalDate dateStart;

    @NotNull(message = "must not be null")
    @Column("documentary_group")
    private DocumentaryGroup group;

    @NotNull(message = "must not be null")
    @Column("type")
    private DocumentaryType type;

    @NotNull(message = "must not be null")
    @Column("content")
    private String content;

    @NotNull(message = "must not be null")
    @Column("signer")
    private UUID signer;

    @NotNull(message = "must not be null")
    @Column("recipient")
    private String recipient;

    @NotNull(message = "must not be null")
    @Column("archive_location")
    private String archiveLocation;

    @Column("sender_or_receiver")
    private UUID senderOrReceiver;

    //////////////



    @Column("attachments_content_file")
    private String attachmentsContentFile;


    @Column("approval_sign_file")
    private String approvalSignFile;

    @Column("attachments")
    private Json attachments=Json.of("[]");

    @Column("reject_note")
    private String rejectNote;

    ////////////////

    @Column("company")
    private String companyId;

    @Column("status")
    private DocumentaryStatus status;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("department")
    private String department;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_by")
    private String deletedBy;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Documentary id(UUID id) {
        this.setId(id);
        return this;
    }



    public Documentary documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }



    public Documentary dateStart(LocalDate dateStart) {
        this.setDateStart(dateStart);
        return this;
    }



    public Documentary group(DocumentaryGroup group) {
        this.setGroup(group);
        return this;
    }



    public Documentary type(DocumentaryType type) {
        this.setType(type);
        return this;
    }



    public Documentary content(String content) {
        this.setContent(content);
        return this;
    }



    public Documentary signer(UUID signer) {
        this.setSigner(signer);
        return this;
    }



    public Documentary recipient(String recipient) {
        this.setRecipient(recipient);
        return this;
    }


    public Documentary archiveLocation(String archiveLocation) {
        this.setArchiveLocation(archiveLocation);
        return this;
    }



    public Documentary senderOrReceiver(UUID senderOrReceiver) {
        this.setSenderOrReceiver(senderOrReceiver);
        return this;
    }










    public Documentary idCompany(String idCompany) {
        this.setIdCompany(idCompany);
        return this;
    }

    public void setIdCompany(String idCompany) {
        this.companyId = idCompany;
    }


    public Documentary status(DocumentaryStatus status) {
        this.setStatus(status);
        return this;
    }



    public Documentary createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }



    public Documentary updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }


    public Documentary deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }


    public Documentary isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }



    public Documentary department(String department) {
        this.setDepartment(department);
        return this;
    }



    public Documentary createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }



    public Documentary updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }



    public Documentary deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }



    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Documentary setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documentary)) {
            return false;
        }
        return getId() != null && getId().equals(((Documentary) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


    public DocumentaryDTO toDto() {
        DocumentaryDTO dto = new DocumentaryDTO();
        dto.setId(this.id);
        dto.setDocumentNumber(this.documentNumber);
        dto.setDateStart(this.dateStart);
        dto.setGroup(this.group);
        dto.setType(this.type);
        dto.setContent(this.content);
        dto.setSigner(this.signer);
        dto.setRecipient(this.recipient);
        dto.setArchiveLocation(this.archiveLocation);
        dto.setSenderOrReceiver(this.senderOrReceiver);

        dto.setIdCompany(this.companyId);
        dto.setStatus(this.status);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        dto.setIsDeleted(this.isDeleted);
        dto.setDeletedAt(this.deletedAt);
        dto.setApprovalSignFile(this.approvalSignFile);
        dto.setAttachmentsContentFile(this.attachmentsContentFile);
        dto.setAttachments(this.attachments);
        return dto;
    }


    public DocumentaryDTO toAllDto() {
        DocumentaryDTO dto = new DocumentaryDTO();
        dto.setId(this.id);
        dto.setDocumentNumber(this.documentNumber);
        dto.setDateStart(this.dateStart);
        dto.setGroup(this.group);
        dto.setType(this.type);
        dto.setContent(this.content);
        dto.setSigner(this.signer);
        dto.setRecipient(this.recipient);
        dto.setArchiveLocation(this.archiveLocation);
        dto.setSenderOrReceiver(this.senderOrReceiver);
        dto.setRejectNote(this.rejectNote);
        dto.setIdCompany(this.companyId);
        dto.setStatus(this.status);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        dto.setIsDeleted(this.isDeleted);
        dto.setDeletedAt(this.deletedAt);
        dto.setDepartment(this.department);
        dto.setCreatedBy(this.createdBy);
        dto.setUpdatedBy(this.updatedBy);
        dto.setDeletedBy(this.deletedBy);
        dto.setApprovalSignFile(this.approvalSignFile);
        dto.setAttachments(this.attachments);
        dto.setAttachmentsContentFile(this.attachmentsContentFile);
        return dto;
    }
}
