package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ConfirmLeaveAttachment.
 */
@Table("confirm_leave_attachment")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmLeaveAttachment implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("file_id")
    private UUID fileId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = { "confirmLeaveAttachments" }, allowSetters = true)
    private ConfirmLeave confirmLeave;

    @Column("confirm_leave_id")
    private UUID confirmLeaveId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public ConfirmLeaveAttachment id(UUID id) {
        this.setId(id);
        return this;
    }



    public ConfirmLeaveAttachment fileId(UUID fileId) {
        this.setFileId(fileId);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ConfirmLeaveAttachment setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setConfirmLeave(ConfirmLeave confirmLeave) {
        this.confirmLeave = confirmLeave;
        this.confirmLeaveId = confirmLeave != null ? confirmLeave.getId() : null;
    }

    public ConfirmLeaveAttachment confirmLeave(ConfirmLeave confirmLeave) {
        this.setConfirmLeave(confirmLeave);
        return this;
    }



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfirmLeaveAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((ConfirmLeaveAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


}
