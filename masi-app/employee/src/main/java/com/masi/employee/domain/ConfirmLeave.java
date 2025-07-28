package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.service.dto.ConfirmLeaveDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ConfirmLeave.
 */
@Table("confirm_leave")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ConfirmLeave implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("submission_date")
    private LocalDate submissionDate;

    @Column("reason")
    private String reason;

    @Column("recruitment_solution")
    private String recruitmentSolution;

    @Column("hr_solution")
    private String hrSolution;

    @Column("leave_date")
    private LocalDate leaveDate;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"confirmLeave"}, allowSetters = true)
    private Set<ConfirmLeaveAttachment> confirmLeaveAttachments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public ConfirmLeave id(UUID id) {
        this.setId(id);
        return this;
    }


    public ConfirmLeave submissionDate(LocalDate submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }


    public ConfirmLeave reason(String reason) {
        this.setReason(reason);
        return this;
    }


    public ConfirmLeave recruitmentSolution(String recruitmentSolution) {
        this.setRecruitmentSolution(recruitmentSolution);
        return this;
    }


    public ConfirmLeave hrSolution(String hrSolution) {
        this.setHrSolution(hrSolution);
        return this;
    }


    public ConfirmLeave leaveDate(LocalDate leaveDate) {
        this.setLeaveDate(leaveDate);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ConfirmLeave setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setConfirmLeaveAttachments(Set<ConfirmLeaveAttachment> confirmLeaveAttachments) {
        if (this.confirmLeaveAttachments != null) {
            this.confirmLeaveAttachments.forEach(i -> i.setConfirmLeave(null));
        }
        if (confirmLeaveAttachments != null) {
            confirmLeaveAttachments.forEach(i -> i.setConfirmLeave(this));
        }
        this.confirmLeaveAttachments = confirmLeaveAttachments;
    }

    public ConfirmLeave confirmLeaveAttachments(Set<ConfirmLeaveAttachment> confirmLeaveAttachments) {
        this.setConfirmLeaveAttachments(confirmLeaveAttachments);
        return this;
    }

    public void addConfirmLeaveAttachment(ConfirmLeaveAttachment confirmLeaveAttachment) {
        this.confirmLeaveAttachments.add(confirmLeaveAttachment);
        confirmLeaveAttachment.setConfirmLeave(this);
    }

    public ConfirmLeave removeConfirmLeaveAttachment(ConfirmLeaveAttachment confirmLeaveAttachment) {
        this.confirmLeaveAttachments.remove(confirmLeaveAttachment);
        confirmLeaveAttachment.setConfirmLeave(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfirmLeave)) {
            return false;
        }
        return getId() != null && getId().equals(((ConfirmLeave) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public ConfirmLeaveDTO toDTO() {
        return ConfirmLeaveDTO.builder()
            .id(this.id)
            .submissionDate(this.submissionDate)
            .reason(this.reason)
            .recruitmentSolution(this.recruitmentSolution)
            .hrSolution(this.hrSolution)
            .leaveDate(this.leaveDate)
            .fileAttachmentIds(this.confirmLeaveAttachments.stream().map(ConfirmLeaveAttachment::getId).collect(Collectors.toList())
            ).build();
    }

}
