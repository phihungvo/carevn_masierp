package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.InterviewMode;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.InterviewProcess;
import com.masi.employee.service.dto.InterviewScheduleDTO;
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
 * A InterviewSchedule.
 */
@Table("interview_schedule")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class InterviewSchedule implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("candidate_name")
    private String candidateName;

    @NotNull(message = "must not be null")
    @Column("interview_date")
    private ZonedDateTime interviewDate;

    @Column("cv_file")
    private String cvFile;

    @NotNull(message = "must not be null")
    @Column("interviewer_id")
    private UUID interviewerId; // người phỏng vấn

    @Column("rate")
    private String rate;

    @Column("process")
    private InterviewProcess process;

    @Column("interview_mode")
    private InterviewMode interviewMode;

    @Column("interview_result")
    private InterviewResult interviewResult;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Transient
    private boolean isPersisted;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Transient
    @JsonIgnoreProperties(value = {"interviewSchedules"}, allowSetters = true)
    private RecruitmentRequest recruitmentRequest;

    @Transient
    private Employee interviewer;

    @Column("recruitment_request_id")
    private UUID recruitmentRequestId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public InterviewSchedule id(UUID id) {
        this.setId(id);
        return this;
    }


    public InterviewSchedule candidateName(String candidateName) {
        this.setCandidateName(candidateName);
        return this;
    }


    public InterviewSchedule interviewDate(ZonedDateTime interviewDate) {
        this.setInterviewDate(interviewDate);
        return this;
    }


    public InterviewSchedule interviewerId(UUID interviewerId) {
        this.setInterviewerId(interviewerId);
        return this;
    }


    public InterviewSchedule rate(String rate) {
        this.setRate(rate);
        return this;
    }


    public InterviewSchedule process(InterviewProcess process) {
        this.setProcess(process);
        return this;
    }


    public InterviewSchedule interviewMode(InterviewMode interviewMode) {
        this.setInterviewMode(interviewMode);
        return this;
    }


    public InterviewSchedule interviewResult(InterviewResult interviewResult) {
        this.setInterviewResult(interviewResult);
        return this;
    }


    public InterviewSchedule lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }


    public InterviewSchedule createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }


    public InterviewSchedule isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public InterviewSchedule setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setRecruitmentRequest(RecruitmentRequest recruitmentRequest) {
        this.recruitmentRequest = recruitmentRequest;
        this.recruitmentRequestId = recruitmentRequest != null ? recruitmentRequest.getId() : null;
    }

    public InterviewSchedule recruitmentRequest(RecruitmentRequest recruitmentRequest) {
        this.setRecruitmentRequest(recruitmentRequest);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterviewSchedule)) {
            return false;
        }
        return getId() != null && getId().equals(((InterviewSchedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterviewSchedule{" +
            "id=" + getId() +
            ", candidateName='" + getCandidateName() + "'" +
            ", interviewDate='" + getInterviewDate() + "'" +

            ", interviewerId='" + getInterviewerId() + "'" +
            ", rate='" + getRate() + "'" +
            ", process='" + getProcess() + "'" +
            ", interviewMode='" + getInterviewMode() + "'" +
            ", interviewResult='" + getInterviewResult() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }

    public InterviewScheduleDTO toDto() {
        InterviewScheduleDTO dto = new InterviewScheduleDTO();
        dto.setId(getId());
        dto.setCandidateName(getCandidateName());
        dto.setInterviewDate(getInterviewDate());
        dto.setCvFile(cvFile);
        dto.setInterviewerId(getInterviewerId());
        dto.setRate(getRate());
        dto.setProcess(getProcess());
        dto.setInterviewMode(getInterviewMode());
        dto.setInterviewResult(getInterviewResult());
        dto.setLastUpdated(getLastUpdated());
        dto.setCreatedDate(getCreatedDate());
        dto.setIsDeleted(getIsDeleted());
        dto.setRecruitmentRequestId(getRecruitmentRequestId());
        dto.setEmail(getEmail());
        dto.setPhoneNumber(getPhoneNumber());
        return dto;
    }
}
