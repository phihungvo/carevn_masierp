package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.PositionEmployee;
import com.masi.employee.service.dto.RecruitmentReviewRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A RecruitmentReviewRequest.
 */
@Table("recruitment_review_request")
@JsonIgnoreProperties(value = { "new" })
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentReviewRequestFull extends RecruitmentReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column("employee_name")
    private String employeeName;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RecruitmentReviewRequestDTO toDto() {
        RecruitmentReviewRequestDTO dto = super.toDto();
        dto.setEmployeeName(this.getEmployeeName());
        return dto;
    }

    public RecruitmentReviewRequestDTO toBriefDto() {
        RecruitmentReviewRequestDTO dto = super.toBriefDto();
        dto.setEmployeeName(this.getEmployeeName());
        return dto;
    }
}
