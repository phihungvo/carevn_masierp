package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.WorkPlace;
import com.masi.employee.service.dto.AnnualLeaveDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AnnualLeave.
 */
@Data
@Table("annual_leave")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnualLeave implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("leave_after_probation") // số phép sau thử việc
    private Integer leaveAfterProbation;

    @NotNull(message = "must not be null")
    @Column("leave_per_year") // số phép reset mỗi năm
    private Integer leavePerYear;

    @NotNull(message = "must not be null")
    @Column("carry_forward_month") // được cộng dồn tới tháng
    private Integer carryForwardMonth;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("work_place")
    private WorkPlace workPlace;

    @Column("updated_at")
    private ZonedDateTime updatedAt;

    @Column("department")
    private String department;

    @Column("company")
    private String company;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public AnnualLeave id(UUID id) {
        this.setId(id);
        return this;
    }

    public AnnualLeave leaveAfterProbation(Integer leaveAfterProbation) {
        this.setLeaveAfterProbation(leaveAfterProbation);
        return this;
    }

    public AnnualLeave leavePerYear(Integer leavePerYear) {
        this.setLeavePerYear(leavePerYear);
        return this;
    }

    public AnnualLeave carryForwardMonth(Integer carryForwardMonth) {
        this.setCarryForwardMonth(carryForwardMonth);
        return this;
    }


    public AnnualLeave lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public AnnualLeave createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public AnnualLeave workPlace(WorkPlace workPlace) {
        this.setWorkPlace(workPlace);
        return this;
    }

    public AnnualLeave updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AnnualLeave setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public AnnualLeaveDTO toDto() {
        AnnualLeaveDTO dto = new AnnualLeaveDTO();
        dto.setLeaveAfterProbation(this.getLeaveAfterProbation());
        dto.setLeavePerYear(this.getLeavePerYear());
        dto.setWorkPlace(this.getWorkPlace());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setId(this.getId());
        dto.setLastUpdated(this.getLastUpdated());
        dto.setCarryForwardMonth(this.getCarryForwardMonth());
        dto.setWorkPlace(this.getWorkPlace());

        return dto;
    }
}
