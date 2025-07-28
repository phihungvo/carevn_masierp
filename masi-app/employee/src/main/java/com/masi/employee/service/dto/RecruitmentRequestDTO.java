package com.masi.employee.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.domain.Workspace;
import com.masi.employee.domain.enumeration.ContractType;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.domain.enumeration.RecruitmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

/**
 * A DTO for the {@link com.masi.employee.domain.RecruitmentRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequestDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private UUID departmentId;

    private Position position;

    private String jobTitle;

    private Float wage;

    private Integer quantity;

    private Integer level;

    private LocalDate startDate;

    private String recruitmentPurposes;

    private UUID employeeId;

    private String requestNotes;

    private String description;

    private Gender gender;

    private LocalDate deadline;

    private RecruitmentStatus oldStatus = RecruitmentStatus.WAITING_APPROVAL;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer numberAdjourn;

    @Lob
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] approvalSign;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approvalSignContentType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rejectNote;

    private RecruitmentStatus status;

    @NotNull(message = "must not be null")
    private ContractType contractType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Collection<InterviewScheduleDTO> interviewSchedules;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isUpdate;

    private boolean isAdjourn;

    private String salaryUnit;

    private UUID replaceForId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {Employee.class})
    private EmployeeDTO replaceFor;
    @Schema(allOf = {Workspace.class})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkspaceDTO department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RecruitmentReviewRequestDTO> listRecruitmentReviews;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean checkApprove;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer numberOfCandidates;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<UUID> empIds;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequestDTO recruitmentRequestDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }


    public void applyUpdate(RecruitmentRequest existingRecruitmentRequest) {
        existingRecruitmentRequest.setDepartmentId(this.departmentId);
        existingRecruitmentRequest.setPosition(this.position);
        existingRecruitmentRequest.setJobTitle(this.jobTitle);
        existingRecruitmentRequest.setWage(this.wage);
        existingRecruitmentRequest.setQuantity(this.quantity);
        existingRecruitmentRequest.setLevel(this.level);
        existingRecruitmentRequest.setStartDate(this.startDate);
        existingRecruitmentRequest.setRecruitmentPurposes(this.recruitmentPurposes);
        existingRecruitmentRequest.setEmployeeId(this.employeeId);
        existingRecruitmentRequest.setRequestNotes(this.requestNotes);
        existingRecruitmentRequest.setDescription(this.description);
        existingRecruitmentRequest.setGender(this.gender);
        existingRecruitmentRequest.setApprovalSign(this.approvalSign);
        existingRecruitmentRequest.setApprovalSignContentType(this.approvalSignContentType);
        existingRecruitmentRequest.setRejectNote(this.rejectNote);
        existingRecruitmentRequest.setContractType(this.contractType);
        existingRecruitmentRequest.setLastUpdated(this.lastUpdated);
        existingRecruitmentRequest.setSalaryUnit(this.salaryUnit);
        existingRecruitmentRequest.setReplaceForId(replaceForId);
        existingRecruitmentRequest.setDeadline(this.deadline);
        if (this.oldStatus != null) {
            existingRecruitmentRequest.setOldStatus(this.oldStatus);
        }
    }

}
