package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.enumeration.UniformOrderProcessStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.UniformOrderProcess} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderProcessDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private UUID approverId;

    private String reason;

    private UniformOrderProcessStatus status;

    private String fileId;

    private String fileName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private FileAttachmentDTO file;

    @NotNull(message = "must not be null")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    private String createBy;

    private ZonedDateTime updateAt;

    private String updateBy;

    private ZonedDateTime deleteAt;

    private String deleteBy;

    private String company;

    private UniformOrderDTO uniformOrder;

    private EmployeeDTO approver;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getApproverId() {
        return approverId;
    }

    public void setApproverId(UUID approverId) {
        this.approverId = approverId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UniformOrderProcessStatus getStatus() {
        return status;
    }

    public void setStatus(UniformOrderProcessStatus status) {
        this.status = status;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public UniformOrderDTO getUniformOrder() {
        return uniformOrder;
    }

    public void setUniformOrder(UniformOrderDTO uniformOrder) {
        this.uniformOrder = uniformOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniformOrderProcessDTO)) {
            return false;
        }

        UniformOrderProcessDTO uniformOrderProcessDTO = (UniformOrderProcessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniformOrderProcessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniformOrderProcessDTO{" +
                "id='" + getId() + "'" +
                ", approverId='" + getApproverId() + "'" +
                ", reason='" + getReason() + "'" +
                ", status='" + getStatus() + "'" +
                ", fileId='" + getFileId() + "'" +
                ", createAt='" + getCreateAt() + "'" +
                ", createBy='" + getCreateBy() + "'" +
                ", updateAt='" + getUpdateAt() + "'" +
                ", updateBy='" + getUpdateBy() + "'" +
                ", deleteAt='" + getDeleteAt() + "'" +
                ", deleteBy='" + getDeleteBy() + "'" +
                ", company='" + getCompany() + "'" +
                ", uniformOrder=" + getUniformOrder() +
                "}";
    }
}
