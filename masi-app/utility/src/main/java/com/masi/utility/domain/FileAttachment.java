package com.masi.utility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FileAttachment.
 */
@Table("file_attachment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class FileAttachment implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("path")
    private String path;

    @NotNull(message = "must not be null")
    @Column("file_size")
    private Long fileSize;

    @NotNull(message = "must not be null")
    @Column("mime_type")
    private String mimeType;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;

    @Column("deleted_at")
    private ZonedDateTime deletedAt;

    @Column("deleted_by")
    private String deletedBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public FileAttachment id(UUID id) {
        this.setId(id);
        return this;
    }



    public FileAttachment name(String name) {
        this.setName(name);
        return this;
    }



    public FileAttachment path(String path) {
        this.setPath(path);
        return this;
    }

    public FileAttachment fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }


    public FileAttachment mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }



    public FileAttachment createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }



    public FileAttachment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }



    public FileAttachment updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }


    public FileAttachment updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }



    public FileAttachment deletedAt(ZonedDateTime deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public FileAttachment deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }



    public FileAttachment company(String company) {
        this.setCompany(company);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public FileAttachment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((FileAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileAttachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", fileSize=" + getFileSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            ", company='" + getCompany() + "'" +
            "}";
    }
}
