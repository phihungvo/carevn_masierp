package com.masi.sale.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.sale.domain.QuotationExport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuotationExportDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private String fileId;

    private String fileName;

    private String company;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    private String updatedBy;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    private String createdBy;

    @NotNull(message = "must not be null")
    private Boolean isDeleted;

    private ZonedDateTime deletedDate;

    private String deletedBy;

    private QuotationDTO quotation;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ZonedDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public QuotationDTO getQuotation() {
        return quotation;
    }

    public void setQuotation(QuotationDTO quotation) {
        this.quotation = quotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuotationExportDTO)) {
            return false;
        }

        QuotationExportDTO quotationExportDTO = (QuotationExportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quotationExportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuotationExportDTO{" +
            "id='" + getId() + "'" +
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
            ", quotation=" + getQuotation() +
            "}";
    }
}
