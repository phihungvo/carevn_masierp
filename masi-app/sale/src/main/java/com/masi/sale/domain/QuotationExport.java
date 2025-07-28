package com.masi.sale.domain;

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
 * A QuotationExport.
 */
@Table("quotation_export")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationExport implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

    @Column("company")
    private String company;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("updated_by")
    private String updatedBy;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @NotNull(message = "must not be null")
    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_date")
    private ZonedDateTime deletedDate;

    @Column("deleted_by")
    private String deletedBy;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "quotationExports", "quotationDetails" }, allowSetters = true)
    private Quotation quotation;

    @Column("quotation_id")
    private UUID quotationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public QuotationExport id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileId() {
        return this.fileId;
    }

    public QuotationExport fileId(String fileId) {
        this.setFileId(fileId);
        return this;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public QuotationExport fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCompany() {
        return this.company;
    }

    public QuotationExport company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public QuotationExport lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public QuotationExport updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public QuotationExport createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public QuotationExport createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public QuotationExport isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTime getDeletedDate() {
        return this.deletedDate;
    }

    public QuotationExport deletedDate(ZonedDateTime deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return this.deletedBy;
    }

    public QuotationExport deletedBy(String deletedBy) {
        this.setDeletedBy(deletedBy);
        return this;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public QuotationExport setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Quotation getQuotation() {
        return this.quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
        this.quotationId = quotation != null ? quotation.getId() : null;
    }

    public QuotationExport quotation(Quotation quotation) {
        this.setQuotation(quotation);
        return this;
    }

    public UUID getQuotationId() {
        return this.quotationId;
    }

    public void setQuotationId(UUID quotation) {
        this.quotationId = quotation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationExport)) {
            return false;
        }
        return getId() != null && getId().equals(((QuotationExport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationExport{" +
            "id=" + getId() +
            ", fileId='" + getFileId() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", company='" + getCompany() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", deletedBy='" + getDeletedBy() + "'" +
            "}";
    }
}
