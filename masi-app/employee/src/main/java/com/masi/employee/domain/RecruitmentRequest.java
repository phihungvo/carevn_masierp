package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.ContractType;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
 * A RecruitmentRequest.
 */
@Table("recruitment_request")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentRequest implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("department_id")
    private UUID departmentId;

    @Transient
    private Workspace department;

    @NotNull(message = "must not be null")
    @Column("position")
    private Position position;

    @NotNull(message = "must not be null")
    @Column("job_title")
    private String jobTitle;

    @NotNull(message = "must not be null")
    @Column("wage")
    private Float wage;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column("level")
    private Integer level;

    @Column("start_date")
    private LocalDate startDate;

    @Column("recruitment_purposes")
    private String recruitmentPurposes;

    @Column("employee_id")
    private UUID employeeId;

    @Column("request_notes")
    private String requestNotes;

    @Column("description")
    private String description;

    @Column("gender")
    private Gender gender;

    @Column("approval_sign")
    private byte[] approvalSign;

    @Column("approval_sign_content_type")
    private String approvalSignContentType;

    @Column("reject_note")
    private String rejectNote;

    @Column("old_status")
    private RecruitmentStatus oldStatus= RecruitmentStatus.WAITING_APPROVAL; // ?? wtf

    @NotNull(message = "must not be null")
    @Column("status")
    private RecruitmentStatus status;

    @NotNull(message = "must not be null")
    @Column("contract_type")
    private ContractType contractType;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("deadline")
    private LocalDate deadline;

    @Column("number_adjourn")
    private Integer numberAdjourn = 0;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"recruitmentRequest"}, allowSetters = true)
    private Collection<InterviewSchedule> interviewSchedules = new HashSet<>();

    @Column("company")
    private String company;

    @Column("salary_unit")
    private String salaryUnit;

    @Column("replace_for_id")
    private UUID replaceForId;

    @Transient
    private Employee replaceFor;


    // jhipster-needle-entity-add-field - JHipster will add fields here


    public UUID getId() {
        return this.id;
    }

    public RecruitmentRequest id(UUID id) {
        this.setId(id);
        return this;
    }


    public RecruitmentRequest requestDescription(UUID requestDescription) {
        this.setDepartmentId(requestDescription);
        return this;
    }

    public RecruitmentRequest position(Position position) {
        this.setPosition(position);
        return this;
    }


    public RecruitmentRequest jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }


    public RecruitmentRequest wage(Float wage) {
        this.setWage(wage);
        return this;
    }


    public RecruitmentRequest quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }


    public RecruitmentRequest level(Integer level) {
        this.setLevel(level);
        return this;
    }


    public RecruitmentRequest startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }


    public RecruitmentRequest recruitmentPurposes(String recruitmentPurposes) {
        this.setRecruitmentPurposes(recruitmentPurposes);
        return this;
    }


    public RecruitmentRequest employeeId(UUID employeeId) {
        this.setEmployeeId(employeeId);
        return this;
    }


    public RecruitmentRequest requestNotes(String requestNotes) {
        this.setRequestNotes(requestNotes);
        return this;
    }


    public RecruitmentRequest description(String description) {
        this.setDescription(description);
        return this;
    }


    public RecruitmentRequest gender(Gender gender) {
        this.setGender(gender);
        return this;
    }


    public RecruitmentRequest approvalSign(byte[] approvalSign) {
        this.setApprovalSign(approvalSign);
        return this;
    }


    public RecruitmentRequest approvalSignContentType(String approvalSignContentType) {
        this.approvalSignContentType = approvalSignContentType;
        return this;
    }


    public RecruitmentRequest rejectNote(String rejectNote) {
        this.setRejectNote(rejectNote);
        return this;
    }


    public RecruitmentRequest status(RecruitmentStatus status) {
        this.setStatus(status);
        return this;
    }


    public RecruitmentRequest contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }


    public RecruitmentRequest lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }


    public RecruitmentRequest createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }


    public RecruitmentRequest isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public RecruitmentRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setInterviewSchedules(Collection<InterviewSchedule> interviewSchedules) {
        if (this.interviewSchedules != null) {
            this.interviewSchedules.forEach(i -> i.setRecruitmentRequest(null));
        }
        if (interviewSchedules != null) {
            interviewSchedules.forEach(i -> i.setRecruitmentRequest(this));
        }
        this.interviewSchedules = interviewSchedules;
    }

    public RecruitmentRequest interviewSchedules(Set<InterviewSchedule> interviewSchedules) {
        this.setInterviewSchedules(interviewSchedules);
        return this;
    }

    public RecruitmentRequest addInterviewSchedules(InterviewSchedule interviewSchedule) {
        this.interviewSchedules.add(interviewSchedule);
        interviewSchedule.setRecruitmentRequest(this);
        return this;
    }

    public RecruitmentRequest removeInterviewSchedules(InterviewSchedule interviewSchedule) {
        this.interviewSchedules.remove(interviewSchedule);
        interviewSchedule.setRecruitmentRequest(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }


    public RecruitmentRequestDTO toDto() {
        var dto = this.toBriefDto();

        dto.setApprovalSign(this.approvalSign);
        dto.setApprovalSignContentType(this.approvalSignContentType);
        dto.setRejectNote(this.rejectNote);
        if (this.interviewSchedules != null)
            dto.setInterviewSchedules(this.interviewSchedules.stream()
                .map(InterviewSchedule::toDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public RecruitmentRequestDTO toBriefDto() {
        RecruitmentRequestDTO dto = new RecruitmentRequestDTO();

        dto.setId(this.id);
        dto.setDepartmentId(this.departmentId);
        dto.setPosition(this.position);
        dto.setJobTitle(this.jobTitle);
        dto.setWage(this.wage);
        dto.setQuantity(this.quantity);
        dto.setLevel(this.level);
        dto.setStartDate(this.startDate);
        dto.setRecruitmentPurposes(this.recruitmentPurposes);
        dto.setEmployeeId(this.employeeId);
        dto.setRequestNotes(this.requestNotes);
        dto.setDescription(this.description);
        dto.setGender(this.gender);
        dto.setStatus(this.status);
        dto.setContractType(this.contractType);
        dto.setLastUpdated(this.lastUpdated);
        dto.setCreatedDate(this.createdDate);
        dto.setIsDeleted(this.isDeleted);
        dto.setSalaryUnit(this.salaryUnit);
        dto.setReplaceForId(this.replaceForId);
        dto.setDeadline(this.deadline);
        dto.setOldStatus(this.oldStatus);
        if (this.department != null)
            dto.setDepartment(this.department.toDTO());
        if (this.replaceFor != null)
            dto.setReplaceFor(this.replaceFor.toDto());
        dto.setNumberAdjourn(this.numberAdjourn);


        return dto;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isPersisted() {
        return isPersisted;
    }

    public void setPersisted(boolean isPersisted) {
        this.isPersisted = isPersisted;
    }


}
