package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.UniformOrderProcessStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UniformOrderProcess.
 */
@Data
@Table("uniform_order_process")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniformOrderProcess implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("approver_id")
    private UUID approverId;

    @Column("reason")
    private String reason;

    @Column("status")
    private UniformOrderProcessStatus status;

    @Column("file_id")
    private String fileId;

    @Column("file_name")
    private String fileName;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private ZonedDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("delete_at")
    private ZonedDateTime deleteAt;

    @Column("delete_by")
    private String deleteBy;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "uniformFormDetails", "uniformOrderProcesses" }, allowSetters = true)
    private UniformOrder uniformOrder;

    @Transient
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Employee approver;

    @Column("uniform_order_id")
    private UUID uniformOrderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public UniformOrderProcess id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getApproverId() {
        return this.approverId;
    }

    public UniformOrderProcess approverId(UUID approverId) {
        this.setApproverId(approverId);
        return this;
    }

    public void setApproverId(UUID approverId) {
        this.approverId = approverId;
    }

    public String getReason() {
        return this.reason;
    }

    public UniformOrderProcess reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UniformOrderProcessStatus getStatus() {
        return this.status;
    }

    public UniformOrderProcess status(UniformOrderProcessStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(UniformOrderProcessStatus status) {
        this.status = status;
    }

    public String getFileId() {
        return this.fileId;
    }

    public UniformOrderProcess fileId(String fileId) {
        this.setFileId(fileId);
        return this;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public UniformOrderProcess createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public UniformOrderProcess createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public UniformOrderProcess updateAt(ZonedDateTime updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public UniformOrderProcess updateBy(String updateBy) {
        this.setUpdateBy(updateBy);
        return this;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getDeleteAt() {
        return this.deleteAt;
    }

    public UniformOrderProcess deleteAt(ZonedDateTime deleteAt) {
        this.setDeleteAt(deleteAt);
        return this;
    }

    public void setDeleteAt(ZonedDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDeleteBy() {
        return this.deleteBy;
    }

    public UniformOrderProcess deleteBy(String deleteBy) {
        this.setDeleteBy(deleteBy);
        return this;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getCompany() {
        return this.company;
    }

    public UniformOrderProcess company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UniformOrderProcess setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UniformOrder getUniformOrder() {
        return this.uniformOrder;
    }

    public void setUniformOrder(UniformOrder uniformOrder) {
        this.uniformOrder = uniformOrder;
        this.uniformOrderId = uniformOrder != null ? uniformOrder.getId() : null;
    }

    public UniformOrderProcess uniformOrder(UniformOrder uniformOrder) {
        this.setUniformOrder(uniformOrder);
        return this;
    }

    public UUID getUniformOrderId() {
        return this.uniformOrderId;
    }

    public void setUniformOrderId(UUID uniformOrder) {
        this.uniformOrderId = uniformOrder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniformOrderProcess)) {
            return false;
        }
        return getId() != null && getId().equals(((UniformOrderProcess) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniformOrderProcess{" +
                "id=" + getId() +
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
                "}";
    }
}
